package com.WebHost.Manager.DTO;

import com.WebHost.Manager.Model.Client;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ClientInfoDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;

    public ClientInfoDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.contactEmail = client.getContactEmail();
        this.phone = client.getPhone();
        this.address = client.getAddress();
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
}
