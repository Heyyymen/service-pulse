package com.servicepulse.security;

import com.servicepulse.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 🎓 FILTRE JWT - Intercepte chaque requête HTTP
 * 
 * Ce filtre vérifie la présence d'un token JWT dans le header Authorization.
 * Si valide, il authentifie l'utilisateur dans le contexte Spring Security.
 * 
 * Analogie JavaScript (Express middleware):
 *   app.use((req, res, next) => {
 *       const token = req.headers.authorization?.split(' ')[1];
 *       if (token) {
 *           const user = jwt.verify(token, secret);
 *           req.user = user;
 *       }
 *       next();
 *   });
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Récupérer le header Authorization
        final String authHeader = request.getHeader("Authorization");
        
        // Si pas de header ou ne commence pas par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraire le token (enlever "Bearer ")
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        // Si username extrait et utilisateur pas déjà authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Charger l'utilisateur depuis la base
            userRepository.findByUsername(username).ifPresent(user -> {
                
                // Vérifier si le token est valide
                if (jwtService.isTokenValid(jwt, username)) {
                    
                    // Créer les autorités (rôles)
                    var authorities = user.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    
                    // Créer UserDetails
                    UserDetails userDetails = User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .authorities(authorities)
                            .build();
                    
                    // Créer le token d'authentification
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Définir l'authentification dans le contexte
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            });
        }
        
        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
