package com.WebHost.Manager.DTO;

import com.WebHost.Manager.Model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class ClientDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;

    private List<DomainDTO> domains;
    private List<HostingDTO> hostings;
    private List<CredentialDTO> credentials;

    public ClientDTO(){

    }
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.contactEmail = client.getContactEmail();
        this.phone = client.getPhone();
        this.address = client.getAddress();
        this.domains = client.getDomains() != null
                ? client.getDomains().stream().map(DomainDTO::new).collect(Collectors.toList())
                : new ArrayList<>();
        this.hostings = client.getHostings() != null
                ? client.getHostings().stream().map(HostingDTO::new).collect(Collectors.toList())
                : new ArrayList<>();
        this.credentials = client.getCredentials() != null
                ? client.getCredentials().stream().map(CredentialDTO::new).collect(Collectors.toList())
                : new ArrayList<>();
    }

    public ClientDTO(Long id, String name, String contactEmail, String phone, String address, List<DomainDTO> domains, List<HostingDTO> hostings, List<CredentialDTO> credentials) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.address = address;
        this.domains = domains;
        this.hostings = hostings;
        this.credentials = credentials;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public List<DomainDTO> getDomains() {
        return domains;
    }

    public List<HostingDTO> getHostings() {
        return hostings;
    }

    public List<CredentialDTO> getCredentials() {
        return credentials;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDomains(List<DomainDTO> domains) {
        this.domains = domains;
    }

    public void setHostings(List<HostingDTO> hostings) {
        this.hostings = hostings;
    }

    public void setCredentials(List<CredentialDTO> credentials) {
        this.credentials = credentials;
    }
}
