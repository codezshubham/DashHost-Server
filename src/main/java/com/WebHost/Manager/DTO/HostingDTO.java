package com.WebHost.Manager.DTO;

import com.WebHost.Manager.Model.Hosting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class HostingDTO {
    private Long id;
    private String provider;
    private LocalDate expiryDate;
    private String plan;
    private String loginUrl;

    public HostingDTO(Hosting hosting) {
        this.id = hosting.getId();
        this.provider = hosting.getProvider();
        this.expiryDate = hosting.getExpiryDate();
        this.plan = hosting.getPlan();
        this.loginUrl = hosting.getLoginUrl();
    }

    public HostingDTO(){

    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
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
}

