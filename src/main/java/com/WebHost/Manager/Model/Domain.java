package com.WebHost.Manager.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private String domainName;
    private String registrar;
    private LocalDate expiryDate;
    private String status; // ACTIVE, EXPIRED

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getDomainName() {
        return domainName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getRegistrar() {
        return registrar;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

