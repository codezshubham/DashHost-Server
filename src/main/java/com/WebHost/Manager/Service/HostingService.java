package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.Client;
import com.WebHost.Manager.Model.Hosting;
import java.util.List;
import java.util.Optional;

public interface HostingService {
    Hosting saveHosting(Hosting hosting);
    Hosting saveHosting1(Hosting request, Client client);
    List<Hosting> getHostingsByClient(Long clientId);
    List<Hosting> getAllHostings();
    Optional<Hosting> getHostingById(Long id);
    void deleteHosting(Long id);
    Hosting saveHosting2(Hosting existing);
}
