package com.servicepulse.controller;

import com.servicepulse.dto.LoginRequest;
import com.servicepulse.dto.LoginResponse;
import com.servicepulse.model.User;
import com.servicepulse.repository.UserRepository;
import com.servicepulse.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 🎓 CONTROLLER AUTH - Gestion de l'authentification
 * 
 * Endpoints pour login/logout
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * POST /api/auth/login
     * Authentifie un utilisateur et retourne un token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        // Chercher l'utilisateur
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        // Vérifier si l'utilisateur est actif
        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Account disabled"));
        }

        // Créer les claims pour le token (rôles, etc.)
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("fullName", user.getFullName());

        // Générer le token JWT
        String token = jwtService.generateToken(user.getUsername(), claims);

        // Retourner la réponse
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/me
     * Retourne les infos de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "fullName", user.getFullName(),
                "roles", user.getRoles()
        ));
    }
}
