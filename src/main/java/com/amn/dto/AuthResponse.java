package com.amn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class AuthResponse {
    private String token;
    private String role;



    // Single argument constructor
    public AuthResponse(String token) {
        this.token = token;
    }

    // Two argument constructor
    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
