package com.WebHost.Manager.Controller;

import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.Exception.UserException;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.UserRepository;
import com.WebHost.Manager.Request.LoginRequest;
import com.WebHost.Manager.Response.AuthResponse;
import com.WebHost.Manager.Service.CustomUserDetails;
import com.WebHost.Manager.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetails customUserDetails;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider, CustomUserDetails customUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetails = customUserDetails;
    }

    @Value("${admin.secret.key}")
    private String storedHashedAdminKey;

    @PostMapping("/signup")
    public ResponseEntity<String> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();
        String role = String.valueOf(user.getRole());
        String adminKey = user.getAdminKey();

        // Check if the email is already registered
        Optional<User> isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist.isPresent()) {
            throw new UserException("Email is already used with another account");
        }

        // If role is ADMIN, verify admin key
        if (Role.ROLE_ADMIN.name().equalsIgnoreCase(role)) {
            if (adminKey == null || !passwordEncoder.matches(adminKey, storedHashedAdminKey)) {
                throw new UserException("Invalid Admin Key");
            }
        }

        // Create and save new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setName(name);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setRole(role);

        userRepository.save(createdUser);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(@RequestBody LoginRequest req) {
        String email = req.getEmail();
        String password = req.getPassword();
        String requestedRole = req.getRole();
        String adminKey = req.getAdminKey();

        // Authenticate user
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // Check for role match
        if (!user.getRole().equalsIgnoreCase(requestedRole)) {
            throw new BadCredentialsException("Role mismatch for this user");
        }

        // If role is ADMIN, validate admin key
        if (requestedRole.equalsIgnoreCase(Role.ROLE_ADMIN.name())) {
            if (adminKey == null || !passwordEncoder.matches(adminKey, storedHashedAdminKey)) {
                throw new BadCredentialsException("Invalid Admin Key");
            }
        }

        // Generate JWT token
        String jwt = jwtTokenProvider.generateToken(authentication);
        System.out.println(jwt + "JWT TOKEN");
        AuthResponse response = new AuthResponse(jwt, true, user.getRole(), user.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Authentication authenticate(String email, String password) {
        var userDetails = customUserDetails.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
