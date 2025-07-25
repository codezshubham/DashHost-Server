package com.WebHost.Manager.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User assignedTo;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Domain> domains;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Hosting> hostings;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Credential> credentials;

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

    public User getAssignedTo() {
        return assignedTo;
    }

    public List<Domain> getDomains() {
        return domains;
    }

    public List<Hosting> getHostings() {
        return hostings;
    }

    public List<Credential> getCredentials() {
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

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public void setHostings(List<Hosting> hostings) {
        this.hostings = hostings;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", assignedTo=" + (assignedTo != null ? assignedTo.getId() : null) +
                '}';
    }

}

