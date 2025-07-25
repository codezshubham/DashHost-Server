package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DomainService {
    Domain saveDomain(Domain domain);

    Domain saveDomain1(Domain request, Client client);

    List<Domain> getDomainsByClient(Long clientId);
    List<Domain> getExpiredDomains(LocalDate date);
    List<Domain> getAllDomains();
    Optional<Domain> getDomainById(Long id);
    void deleteDomain(Long id);

    Domain saveDomain3(Domain existing);
}

