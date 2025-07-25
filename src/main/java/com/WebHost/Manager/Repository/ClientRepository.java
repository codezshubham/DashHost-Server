package com.WebHost.Manager.Repository;

import com.WebHost.Manager.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByAssignedToId(Long userId);

    Optional<Client> findByContactEmail(String contactEmail);

}

