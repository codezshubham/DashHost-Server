package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.User;
import com.WebHost.Manager.Repository.ClientRepository;
import com.WebHost.Manager.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientServiceImpl(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getClientsByUser(Long userId) {
        return clientRepository.findByAssignedToId(userId);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Optional<Client> findByContactEmail(String contactEmail) {
        return clientRepository.findByContactEmail(contactEmail);
    }

}

