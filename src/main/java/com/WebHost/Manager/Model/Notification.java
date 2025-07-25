package com.WebHost.Manager.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    private String message;
    private LocalDate scheduledDate;
    private boolean sentStatus;

    public Notification(){

    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public boolean isSentStatus() {
        return sentStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setSentStatus(boolean sentStatus) {
        this.sentStatus = sentStatus;
    }
}

