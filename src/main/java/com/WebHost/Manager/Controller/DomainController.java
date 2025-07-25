package com.WebHost.Manager.Controller;

import com.WebHost.Manager.Configuration.JwtTokenProvider;
import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Domain;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.UserRepository;
import com.WebHost.Manager.Response.ApiResponse;
import com.WebHost.Manager.Service.ClientService;
import com.WebHost.Manager.Service.DomainService;
import com.WebHost.Manager.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/domains")
public class DomainController {

    private final DomainService domainService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ClientService clientService;

    public DomainController(DomainService domainService,
                            JwtTokenProvider jwtTokenProvider,
                            UserService userService,
                            ClientService clientService) {
        this.domainService = domainService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.clientService = clientService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Domain request,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long clientId = request.getClient().getId();
        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        Client client = clientOpt.get();
//        if (!client.getUser().getId().equals(user.getId()) &&
//                user.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to create domain for this client");
//        }

        Domain savedDomain = domainService.saveDomain(request);
        return ResponseEntity.ok(new ApiResponse("Domain created successfully", savedDomain));
    }

    @PostMapping("/add/{clientId}")
    public ResponseEntity<?> addDomainWithClientId(@PathVariable Long clientId,
                                                   @RequestBody Domain request,
                                                   @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        // Set the client for the domain from the path variable
        Client client = clientOpt.get();

        Domain savedDomain = domainService.saveDomain1(request, client);
        return ResponseEntity.ok(new ApiResponse("Domain added successfully", savedDomain));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
//        if (user == null || user.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can view all domains");
//        }
        return ResponseEntity.ok(domainService.getAllDomains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id,
                                     @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Domain> domainOpt = domainService.getDomainById(id);
        if (domainOpt.isEmpty()) return ResponseEntity.notFound().build();

        Domain domain = domainOpt.get();
        Client client = domain.getClient();
//        if (!client.getUser().getId().equals(user.getId()) &&
//                user.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view this domain");
//        }

        return ResponseEntity.ok(domain);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getByClient(@PathVariable Long clientId,
                                         @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Client> clientOpt = clientService.getClientById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");

        Client client = clientOpt.get();
//        if (!client.getUser().getId().equals(user.getId()) &&
//                user.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to view domains for this client");
//        }

        return ResponseEntity.ok(domainService.getDomainsByClient(clientId));
    }

    @GetMapping("/expired")
    public ResponseEntity<?> getExpired(@RequestParam String beforeDate,
                                        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
//        if (user == null || user.getRole() != Role.ROLE_ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can view expired domains");
//        }

        LocalDate date = LocalDate.parse(beforeDate);
        return ResponseEntity.ok(domainService.getExpiredDomains(date));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDomain(@PathVariable Long id,
                                          @RequestBody Domain updatedDomain,
                                          @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> currentUser = userService.getUserByEmail(email);
        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<Domain> existingOpt = domainService.getDomainById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Domain not found");
        }

        Domain existing = existingOpt.get();

        // Update fields
        existing.setDomainName(updatedDomain.getDomainName());
        existing.setRegistrar(updatedDomain.getRegistrar());
        existing.setStatus(updatedDomain.getStatus());
        existing.setExpiryDate(updatedDomain.getExpiryDate());

        Domain saved = domainService.saveDomain3(existing);
        return ResponseEntity.ok(new ApiResponse("Domain updated successfully", saved));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromJwtToken(token);
        Optional<User> user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Domain> domainOpt = domainService.getDomainById(id);
        if (domainOpt.isEmpty()) return ResponseEntity.notFound().build();

        domainService.deleteDomain(id);
        return ResponseEntity.ok("Domain deleted successfully");
    }
}
