package com.WebHost.Manager.Repository;


import com.WebHost.Manager.Model.Hosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostingRepository extends JpaRepository<Hosting, Long> {
    List<Hosting> findByClientId(Long clientId);
}

