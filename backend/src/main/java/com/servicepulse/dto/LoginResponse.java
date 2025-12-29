package com.servicepulse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 🎓 DTO - Login Response
 * 
 * Données retournées au client après une connexion réussie
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String fullName;
    private Set<String> roles;
}
