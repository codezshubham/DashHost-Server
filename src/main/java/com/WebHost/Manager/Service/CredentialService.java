package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Credential;
import java.util.List;
import java.util.Optional;

public interface CredentialService {
    Credential saveCredential(Credential credential);

    Credential saveCredential1(Credential request, Client client);

    Optional<Client> findByContactEmail(String contactEmail);

    List<Credential> getCredentialsByClient(Long clientId);
    List<Credential> getAllCredentials();
    Optional<Credential> getCredentialById(Long id);

    Credential updateCredential(Long id, Credential request);

    void deleteCredential(Long id);
}

