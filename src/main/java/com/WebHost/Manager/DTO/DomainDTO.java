package com.WebHost.Manager.DTO;

import com.WebHost.Manager.Model.Domain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
public class DomainDTO {
    private Long id;
    private String domainName;
    private String registrar;
    private LocalDate expiryDate;
    private String status; // ACTIVE, EXPIRED

    public DomainDTO(Domain domain) {
        this.id = domain.getId();
        this.domainName = domain.getDomainName();
        this.registrar = domain.getRegistrar();
        this.expiryDate = domain.getExpiryDate();
        this.status = domain.getStatus();
    }

    public DomainDTO(){

    }

    public Long getId() {
        return id;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getRegistrar() {
        return registrar;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
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


