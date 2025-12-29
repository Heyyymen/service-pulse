package com.servicepulse.config;

import com.servicepulse.model.User;
import com.servicepulse.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 🎓 INITIALISATION DES DONNÉES - Crée les utilisateurs de test
 * 
 * Cette classe est exécutée au démarrage pour créer les utilisateurs admin et lecteur
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Créer l'admin si n'existe pas
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .fullName("Admin User")
                    .roles(Set.of("ROLE_ADMIN", "ROLE_LECTEUR"))
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Utilisateur admin créé (username: admin, password: password)");
        }

        // Créer le lecteur si n'existe pas
        if (!userRepository.existsByUsername("lecteur")) {
            User lecteur = User.builder()
                    .username("lecteur")
                    .password(passwordEncoder.encode("password"))
                    .fullName("Lecteur User")
                    .roles(Set.of("ROLE_LECTEUR"))
                    .enabled(true)
                    .build();
            userRepository.save(lecteur);
            System.out.println("✅ Utilisateur lecteur créé (username: lecteur, password: password)");
        }
    }
}
