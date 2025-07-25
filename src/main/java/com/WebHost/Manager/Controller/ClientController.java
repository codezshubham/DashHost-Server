package com.WebHost.Manager.Controller;

import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.DTO.ClientInfoDTO;
import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.UserRepository;
import com.WebHost.Manager.Service.ActivityLogServiceImpl;
import com.WebHost.Manager.Service.ClientService;
import com.WebHost.Manager.Service.UserService;
import com.WebHost.Manager.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ActivityLogServiceImpl activityLogService;

    public ClientController(ClientService clientService, JwtTokenProvider jwtTokenProvider, UserService userService, ActivityLogServiceImpl activityLogService) {
        this.clientService = clientService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.activityLogService = activityLogService;
    }


    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createClient(@RequestBody Client client,
                                                            @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userOptional.get();

        // ✅ Check for existing client by contactEmail
        Optional<Client> existingClient = clientService.findByContactEmail(client.getContactEmail());
        if (existingClient.isPresent()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Client already exists with this email.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // 409 Conflict
        }

        client.setAssignedTo(user);
        Client savedClient = clientService.saveClient(client);

        // ✅ Log creation activity
        activityLogService.log(
                user.getId().toString(),
                user.getName(),
                user.getRole(),
                "Created client",
                "Client name: " + savedClient.getName()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Client created successfully");
        response.put("id", savedClient.getId());

        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @RequestHeader("Authorization") String token) {
        try {
            System.out.println("ClientId: " + id);

            // Extract user info from token
            String email = jwtTokenProvider.getEmailFromJwtToken(token);
            Optional<User> currentUser = userService.getUserByEmail(email);

            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Not authorized
            }

            Optional<Client> clientOpt = clientService.getClientById(id);

            if (clientOpt.isEmpty()) {
                return ResponseEntity.notFound().build();  // Client not found
            }

            Client client = clientOpt.get();

            // Create and return DTO
            ClientInfoDTO dto = new ClientInfoDTO(client);
            System.out.println(dto);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            e.printStackTrace();  // Optional: Log exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token or unauthorized access");
        }
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
//        if (user == null || user.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//        }
        return ResponseEntity.ok(clientService.getAllClients());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId,
                                         @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

//        if (currentUser.getRole() != Role.ROLE_ADMIN && !Objects.equals(currentUser.getId(), userId)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view these clients.");
//        }

        return ResponseEntity.ok(clientService.getClientsByUser(userId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id,
                                          @RequestBody Client updatedClient,
                                          @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        Optional<Client> existingClientOpt = clientService.getClientById(id);
        if (existingClientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        Client existingClient = existingClientOpt.get();

        // Update fields
        existingClient.setName(updatedClient.getName());
        existingClient.setAddress(updatedClient.getAddress());
        existingClient.setPhone(updatedClient.getPhone());
        existingClient.setContactEmail(updatedClient.getContactEmail());

        // Save updated client
        Client savedClient = clientService.saveClient(existingClient);

        // ✅ Log the update activity
        User user = currentUser.get();
        activityLogService.log(
                user.getId().toString(),
                user.getName(),
                user.getRole(),
                "Updated client",
                "Updated client name: " + savedClient.getName()
        );

        return ResponseEntity.ok(savedClient);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        clientService.deleteClient(id);

        // ✅ Log the delete activity
        User user = currentUser.get();
        activityLogService.log(
                user.getId().toString(),
                user.getName(),
                user.getRole(),
                "Deleted client",
                "Deleted client with ID: " + id
        );
        return ResponseEntity.ok().body("Client deleted successfully.");
    }
}
