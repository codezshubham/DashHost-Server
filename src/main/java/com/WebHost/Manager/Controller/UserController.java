package com.WebHost.Manager.Controller;
import com.WebHost.Manager.DTO.ClientDTO;
import com.WebHost.Manager.DTO.UserDTO;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.UserRepository;
import com.WebHost.Manager.Service.ActivityLogServiceImpl;
import com.WebHost.Manager.Service.UserService;
import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.Exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ActivityLogServiceImpl activityLogService;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, ActivityLogServiceImpl activityLogService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user, @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);

        User created = userService.saveUser(user);

        // ✅ Log activity by the current authenticated user (not the one being created)
        currentUser.ifPresent(actor -> {
            activityLogService.log(
                    actor.getId().toString(),
                    actor.getName(),
                    actor.getRole(),
                    "Created new user",
                    "Created user with username: " + user.getName()
            );
        });

        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(token);
            Optional<User> user =userService.getUserByEmail(email);

            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            // Log the exception to see more details
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email, @RequestHeader("Authorization") String token) {
        try {
            String requesterEmail = jwtTokenProvider.getEmailFromJwtToken(token);
            Optional<User> currentUser = Optional.ofNullable(userService.getUserByEmail(requesterEmail)
                    .orElseThrow(() -> new UserException("User not found")));

//            if (currentUser.getRole() != Role.ROLE_ADMIN && !requesterEmail.equals(email)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }

            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAll(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(token);
            User currentUser = userService.getUserByEmail(email)
                    .orElseThrow(() -> new UserException("User not found"));

            // Optional: Role-based access control (uncomment if needed)
            // if (currentUser.getRole() != Role.ROLE_ADMIN) {
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            // }

            List<User> allUsers = userService.getAllUsers();

            // Convert to DTOs
            List<UserDTO> allUserDTOs = allUsers.stream()
                    .map(UserDTO::new)
                    .toList(); // Java 16+; use .collect(Collectors.toList()) if using Java 8-15

            return ResponseEntity.ok(allUserDTOs);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenProvider.getEmailFromJwtToken(token);
            User currentUser = userService.getUserByEmail(email)
                    .orElseThrow(() -> new UserException("User not found"));

            // Optionally fetch the user to be deleted for better logging
            Optional<User> deletedUser = userService.getUserById(id);

            userService.deleteUser(id);

            // ✅ Log the deletion action
            activityLogService.log(
                    currentUser.getId().toString(),
                    currentUser.getName(),
                    currentUser.getRole(),
                    "Deleted user",
                    deletedUser.map(u -> "Deleted user with username: " + u.getName())
                            .orElse("Deleted user with ID: " + id)
            );

            return ResponseEntity.ok("User deleted successfully.");
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/my-clients")
    public ResponseEntity<?> getMyClients(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("Calling get/my-clients");
            String email = jwtTokenProvider.getEmailFromJwtToken(token);
            System.out.println("Token received: " + token);
            System.out.println("Email received: " + email);

            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new UserException("User not found"));

            List<ClientDTO> clientDTOs = user.getClients()
                    .stream()
                    .map(ClientDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(clientDTOs);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching your clients.");
        }
    }


}
