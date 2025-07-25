package com.WebHost.Manager.Repository;

import com.WebHost.Manager.Model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByClientId(Long clientId);
}

