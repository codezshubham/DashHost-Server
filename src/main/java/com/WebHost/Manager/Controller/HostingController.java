package com.WebHost.Manager.Controller;

import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Hosting;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.UserRepository;
import com.WebHost.Manager.Response.ApiResponse;
import com.WebHost.Manager.Service.ClientService;
import com.WebHost.Manager.Service.HostingService;
import com.WebHost.Manager.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hostings")
public class HostingController {

    private final HostingService hostingService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ClientService clientService;

    public HostingController(HostingService hostingService, JwtTokenProvider jwtTokenProvider,
                             UserService userService, UserRepository userRepository, ClientService clientService) {
        this.hostingService = hostingService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clientService = clientService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Hosting request,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long clientId = request.getClient().getId();
        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        Client client = clientOpt.get();

        Hosting savedHosting = hostingService.saveHosting(request);
        return ResponseEntity.ok(new ApiResponse("Hosting created successfully", savedHosting));
    }

    @PostMapping("/add/{clientId}")
    public ResponseEntity<?> addHosting(@RequestBody Hosting request,
                                        @PathVariable Long clientId,
                                        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
        }

        Client client = clientOpt.get();

        Hosting savedHosting = hostingService.saveHosting1(request, client);
        return ResponseEntity.ok(new ApiResponse("Hosting created successfully", savedHosting));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
//        if (currentUser == null || currentUser.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can view all hostings");
//        }
        return ResponseEntity.ok(hostingService.getAllHostings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Hosting> hostingOpt = hostingService.getHostingById(id);
        if (hostingOpt.isEmpty()) return ResponseEntity.notFound().build();

        Hosting hosting = hostingOpt.get();
        Client client = hosting.getClient();

//        if (!client.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view this hosting");
//        }

        return ResponseEntity.ok(hosting);
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

//        if (!client.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view hostings for this client");
//        }

        return ResponseEntity.ok(hostingService.getHostingsByClient(clientId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateHosting(@PathVariable Long id,
                                           @RequestBody Hosting updatedHosting,
                                           @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<Hosting> existingOpt = hostingService.getHostingById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hosting not found");
        }

        Hosting existing = existingOpt.get();

        // Update fields
        existing.setProvider(updatedHosting.getProvider());
        existing.setPlan(updatedHosting.getPlan());
        existing.setExpiryDate(updatedHosting.getExpiryDate());
        existing.setLoginUrl(updatedHosting.getLoginUrl());

        Hosting saved = hostingService.saveHosting2(existing);
        return ResponseEntity.ok(new ApiResponse("Hosting updated successfully", saved));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Hosting> hostingOpt = hostingService.getHostingById(id);
        if (hostingOpt.isEmpty()) return ResponseEntity.notFound().build();

        hostingService.deleteHosting(id);
        return ResponseEntity.ok("Hosting deleted successfully");
    }
}
