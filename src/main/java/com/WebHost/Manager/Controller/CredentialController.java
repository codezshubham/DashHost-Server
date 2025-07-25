package com.WebHost.Manager.Controller;

import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Credential;

import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.UserRepository;
import com.WebHost.Manager.Response.ApiResponse;

import com.WebHost.Manager.Service.ClientService;
import com.WebHost.Manager.Service.CredentialService;
import com.WebHost.Manager.Service.UserService;

import com.WebHost.Manager.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ClientService clientService;

    public CredentialController(CredentialService credentialService,
                                JwtTokenProvider jwtTokenProvider,
                                UserService userService,
                                ClientService clientService) {
        this.credentialService = credentialService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientService = clientService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Credential request,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser==null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long clientId = request.getClient().getId();
        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        Client client = clientOpt.get();

        Credential savedCredential = credentialService.saveCredential(request);
        return ResponseEntity.ok(new ApiResponse("Credential created successfully", savedCredential));
    }

    @PostMapping("/add/{clientId}")
    public ResponseEntity<?> addCredentialWithClientId(@PathVariable Long clientId,
                                                       @RequestBody Credential request,
                                                       @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        Client client = clientOpt.get();

        Credential savedCredential = credentialService.saveCredential1(request, client);
        return ResponseEntity.ok(new ApiResponse("Credential added successfully", savedCredential));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
//        if (currentUser == null || currentUser.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can view all credentials");
//        }
        return ResponseEntity.ok(credentialService.getAllCredentials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Credential> credentialOpt = credentialService.getCredentialById(id);
        if (credentialOpt.isEmpty()) return ResponseEntity.notFound().build();

        Credential credential = credentialOpt.get();
        Client client = credential.getClient();
//        if (!client.getUser().getId().equals(currentUser.getId()) &&
//                currentUser.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view this credential");
//        }

        return ResponseEntity.ok(credential);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getByClient(@PathVariable Long clientId,
                                         @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        Client client = clientOpt.get();
//        if (!client.getUser().getId().equals(currentUser.getId()) &&
//                currentUser.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view credentials for this client");
//        }

        return ResponseEntity.ok(credentialService.getCredentialsByClient(clientId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCredential(@PathVariable Long id,
                                              @RequestBody Credential updatedCredential,
                                              @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            Credential saved = credentialService.updateCredential(id, updatedCredential);
            return ResponseEntity.ok(new ApiResponse("Credential updated successfully", saved));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Credential> credentialOpt = credentialService.getCredentialById(id);
        if (credentialOpt.isEmpty()) return ResponseEntity.notFound().build();

        credentialService.deleteCredential(id);
        return ResponseEntity.ok("Credential deleted successfully");
    }
}
