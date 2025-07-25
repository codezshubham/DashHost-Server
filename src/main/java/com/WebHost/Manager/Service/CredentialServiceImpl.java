package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Credential;
import com.WebHost.Manager.Repository.ClientRepository;
import com.WebHost.Manager.Repository.CredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final ClientRepository clientRepository;

    public CredentialServiceImpl(CredentialRepository credentialRepository, ClientRepository clientRepository) {
        this.credentialRepository = credentialRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Credential saveCredential(Credential request) {
        Credential credential = new Credential();
        credential.setType(request.getType());
        credential.setUsername(request.getUsername());
        credential.setEncryptedPassword(request.getEncryptedPassword());

        // Use request.getClient().getId() to get the correct client ID
        Client client = clientRepository.findById(request.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + request.getClient().getId()));

        credential.setClient(client);  // Make sure client is set properly

        // Save the credential and return
        return credentialRepository.save(credential);
    }

    @Override
    public Credential saveCredential1(Credential request, Client client) {
        Credential credential = new Credential();
        credential.setType(request.getType());
        credential.setUsername(request.getUsername());
        credential.setEncryptedPassword(request.getEncryptedPassword());
        credential.setClient(client);
        // Save the credential and return
        return credentialRepository.save(credential);
    }

    @Override
    public Optional<Client> findByContactEmail(String contactEmail) {
        return clientRepository.findByContactEmail(contactEmail);
    }

    @Override
    public List<Credential> getCredentialsByClient(Long clientId) {
        return credentialRepository.findByClientId(clientId);
    }

    @Override
    public List<Credential> getAllCredentials() {
        return credentialRepository.findAll();
    }

    @Override
    public Optional<Credential> getCredentialById(Long id) {
        return credentialRepository.findById(id);
    }

    @Override
    public Credential updateCredential(Long id, Credential request) {
        Credential existing = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found with ID: " + id));

        existing.setType(request.getType());
        existing.setUsername(request.getUsername());
        existing.setEncryptedPassword(request.getEncryptedPassword());
        return credentialRepository.save(existing);
    }

    @Override
    public void deleteCredential(Long id) {
        credentialRepository.deleteById(id);
    }
}

