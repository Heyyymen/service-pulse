package com.servicepulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🎓 POINT D'ENTRÉE DE L'APPLICATION
 * 
 * Analogie JavaScript:
 * C'est comme le fichier "index.js" ou "app.js" dans Express où tu fais:
 *   const app = express();
 *   app.listen(3000);
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 CONCEPT: @SpringBootApplication
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Cette annotation est un "combo" de 3 annotations:
 * 
 * 1. @Configuration     → Dit à Spring "cette classe contient de la config"
 * 2. @EnableAutoConfiguration → Spring configure automatiquement les beans
 * 3. @ComponentScan    → Spring scanne ce package et ses sous-packages
 *                        pour trouver les @Controller, @Service, etc.
 * 
 * En JS, c'est comme si Express faisait automatiquement:
 *   - require() de tous tes fichiers routes/
 *   - Configuration automatique de body-parser, cors, etc.
 */
@SpringBootApplication
public class ServicePulseApplication {

    public static void main(String[] args) {
        // Lance l'application Spring Boot
        // Équivalent JS: app.listen(port, () => console.log('Server started'))
        SpringApplication.run(ServicePulseApplication.class, args);
    }

}
