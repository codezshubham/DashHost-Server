package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.User;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client saveClient(Client client);
    List<Client> getClientsByUser(Long userId);
    List<Client> getAllClients();
    Optional<Client> getClientById(Long id);
    void deleteClient(Long id);

    Optional<Client> findByContactEmail(String contactEmail);
}

