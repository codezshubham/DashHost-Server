package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Domain;
import com.WebHost.Manager.Repository.ClientRepository;
import com.WebHost.Manager.Repository.DomainRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;
    private final ClientRepository clientRepository;

    public DomainServiceImpl(DomainRepository domainRepository, ClientRepository clientRepository) {
        this.domainRepository = domainRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Domain saveDomain(Domain request) {
        Domain domain = new Domain();
        domain.setDomainName(request.getDomainName());
        domain.setRegistrar(request.getRegistrar());
        domain.setExpiryDate(request.getExpiryDate());
        domain.setStatus(request.getStatus());

        Long clientId = request.getClient().getId(); // Extracting ID from nested object
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        domain.setClient(client);

        return domainRepository.save(domain);
    }

    @Override
    public Domain saveDomain1(Domain request, Client client) {
        Domain domain = new Domain();
        domain.setDomainName(request.getDomainName());
        domain.setRegistrar(request.getRegistrar());
        domain.setExpiryDate(request.getExpiryDate());
        domain.setStatus(request.getStatus());
        domain.setClient(client);

        return domainRepository.save(domain);
    }

    @Override
    public List<Domain> getDomainsByClient(Long clientId) {
        return domainRepository.findByClientId(clientId);
    }

    @Override
    public List<Domain> getExpiredDomains(LocalDate date) {
        return domainRepository.findByExpiryDateBefore(date);
    }

    @Override
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    @Override
    public Optional<Domain> getDomainById(Long id) {
        return domainRepository.findById(id);
    }

    @Override
    public void deleteDomain(Long id) {
        domainRepository.deleteById(id);
    }

    @Override
    public Domain saveDomain3(Domain existing) {
        return domainRepository.save(existing);
    }
}

