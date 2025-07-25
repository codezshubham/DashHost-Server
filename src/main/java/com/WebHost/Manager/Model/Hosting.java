package com.WebHost.Manager.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Hosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private String provider;
    private LocalDate expiryDate;
    private String plan;
    private String loginUrl;

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getProvider() {
        return provider;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getPlan() {
        return plan;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}

