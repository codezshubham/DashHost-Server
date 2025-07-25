package com.WebHost.Manager.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String jwt;
    private String role;
    private boolean status;
    private Long userId;  // Added userId field

    // Custom constructor (used when role and userId are available)
    public AuthResponse(String jwt, boolean status, String role, Long userId) {
        this.jwt = jwt;
        this.status = status;
        this.role = role;
        this.userId = userId;
    }

    // Custom constructor (used when role is not needed)
    public AuthResponse(String jwt, boolean status) {
        this.jwt = jwt;
        this.status = status;
    }
}

