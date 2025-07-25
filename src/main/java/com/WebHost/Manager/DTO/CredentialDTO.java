package com.WebHost.Manager.DTO;

import com.WebHost.Manager.Model.Credential;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CredentialDTO {
    private Long id;
    private String type;     // e.g., cPanel, Email, FTP
    private String username;
    private String encryptedPassword;

    public CredentialDTO(Credential credential) {
        this.id = credential.getId();
        this.type = credential.getType();
        this.username = credential.getUsername();
        this.encryptedPassword = credential.getEncryptedPassword(); // or mask it if needed
    }

    public CredentialDTO(){

    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}

