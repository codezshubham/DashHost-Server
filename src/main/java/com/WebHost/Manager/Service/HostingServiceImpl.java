package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Hosting;
import com.WebHost.Manager.Repository.ClientRepository;
import com.WebHost.Manager.Repository.HostingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostingServiceImpl implements HostingService {

    private final HostingRepository hostingRepository;
    private final ClientRepository clientRepository;

    public HostingServiceImpl(HostingRepository hostingRepository, ClientRepository clientRepository) {
        this.hostingRepository = hostingRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Hosting saveHosting(Hosting request) {
        Hosting hosting = new Hosting();
        hosting.setProvider(request.getProvider());
        hosting.setExpiryDate(request.getExpiryDate());
        hosting.setPlan(request.getPlan());
        hosting.setLoginUrl(request.getLoginUrl());

        Long clientId = request.getClient().getId();
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        hosting.setClient(client);

        return hostingRepository.save(hosting);
    }

    @Override
    public Hosting saveHosting1(Hosting request, Client client) {
        Hosting hosting = new Hosting();
        hosting.setProvider(request.getProvider());
        hosting.setExpiryDate(request.getExpiryDate());
        hosting.setPlan(request.getPlan());
        hosting.setLoginUrl(request.getLoginUrl());
        hosting.setClient(request.getClient());

        return hostingRepository.save(hosting);
    }

    public Hosting saveHosting2(Hosting hosting) {
        return hostingRepository.save(hosting);
    }

    @Override
    public List<Hosting> getHostingsByClient(Long clientId) {
        return hostingRepository.findByClientId(clientId);
    }

    @Override
    public List<Hosting> getAllHostings() {
        return hostingRepository.findAll();
    }

    @Override
    public Optional<Hosting> getHostingById(Long id) {
        return hostingRepository.findById(id);
    }

    @Override
    public void deleteHosting(Long id) {
        hostingRepository.deleteById(id);
    }
}

