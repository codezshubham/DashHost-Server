package com.WebHost.Manager.Repository;

import com.WebHost.Manager.Model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DomainRepository extends JpaRepository<Domain, Long> {
    List<Domain> findByExpiryDateBefore(LocalDate date);
    List<Domain> findByClientId(Long clientId);
    List<Domain> findByExpiryDate(LocalDate expiryDate);
}

