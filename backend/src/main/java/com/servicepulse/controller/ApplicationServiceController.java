package com.servicepulse.controller;

import com.servicepulse.model.ApplicationService;
import com.servicepulse.model.ServiceStatus;
import com.servicepulse.service.ApplicationServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 🎓 CONTROLLER REST - Points d'entrée de l'API
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 QU'EST-CE QU'UN CONTROLLER EN SPRING ?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Le Controller définit les ENDPOINTS de ton API REST.
 * 
 * Analogie JavaScript avec Express :
 * 
 *   // Express.js
 *   const router = express.Router();
 *   router.get('/services', (req, res) => { ... });
 *   router.post('/services', (req, res) => { ... });
 *   router.put('/services/:id', (req, res) => { ... });
 *   router.delete('/services/:id', (req, res) => { ... });
 * 
 *   // Spring Boot (ce fichier)
 *   @GetMapping → router.get()
 *   @PostMapping → router.post()
 *   @PutMapping → router.put()
 *   @DeleteMapping → router.delete()
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 ANNOTATIONS DU CONTROLLER
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * @RestController = @Controller + @ResponseBody
 *                   Dit à Spring "les méthodes retournent du JSON directement"
 * 
 * @RequestMapping("/api/services") = Préfixe de toutes les routes
 *                                    Comme app.use('/api/services', router) en Express
 * 
 * @CrossOrigin = Autorise les requêtes depuis d'autres origines (CORS)
 *                Nécessaire pour que Angular puisse appeler l'API
 */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // En prod, limiter aux domaines autorisés
public class ApplicationServiceController {

    private final ApplicationServiceService serviceService;

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 📖 GET - Lecture de données
     * ═══════════════════════════════════════════════════════════════════════════
     */

    /**
     * GET /api/services
     * Récupère tous les services
     * 
     * Équivalent Express :
     *   router.get('/', async (req, res) => {
     *       const services = await Service.find();
     *       res.json(services);
     *   });
     */
    @GetMapping
    public ResponseEntity<List<ApplicationService>> getAllServices() {
        List<ApplicationService> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    /**
     * GET /api/services/{id}
     * Récupère un service par son ID
     * 
     * @PathVariable = Extrait la valeur de l'URL (comme req.params.id en Express)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationService> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)  // Si trouvé → 200 OK
                .orElse(ResponseEntity.notFound().build());  // Si non trouvé → 404
    }

    /**
     * GET /api/services/status/{status}
     * Récupère les services filtrés par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApplicationService>> getServicesByStatus(@PathVariable ServiceStatus status) {
        return ResponseEntity.ok(serviceService.getServicesByStatus(status));
    }

    /**
     * GET /api/services/alerts
     * Récupère les services en alerte (non opérationnels)
     */
    @GetMapping("/alerts")
    public ResponseEntity<List<ApplicationService>> getAlerts() {
        return ResponseEntity.ok(serviceService.getNonOperationalServices());
    }

    /**
     * GET /api/services/search?q=xxx
     * Recherche des services par nom
     * 
     * @RequestParam = Extrait un paramètre de query string (comme req.query.q en Express)
     */
    @GetMapping("/search")
    public ResponseEntity<List<ApplicationService>> searchServices(@RequestParam("q") String query) {
        return ResponseEntity.ok(serviceService.searchServices(query));
    }

    /**
     * GET /api/services/health-summary
     * Retourne un résumé de la santé des services
     */
    @GetMapping("/health-summary")
    public ResponseEntity<Map<String, Object>> getHealthSummary() {
        long total = serviceService.countServices();
        boolean allOperational = serviceService.areAllServicesOperational();
        List<ApplicationService> alerts = serviceService.getNonOperationalServices();
        
        Map<String, Object> summary = Map.of(
            "totalServices", total,
            "allOperational", allOperational,
            "alertCount", alerts.size(),
            "alerts", alerts
        );
        
        return ResponseEntity.ok(summary);
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * ✏️ POST - Création de données
     * ═══════════════════════════════════════════════════════════════════════════
     */

    /**
     * POST /api/services
     * Crée un nouveau service
     * 
     * @RequestBody = Parse le JSON du body de la requête (comme req.body en Express)
     * @Valid = Applique les validations définies dans l'entité (@NotBlank, @Size, etc.)
     * 
     * Équivalent Express :
     *   router.post('/', async (req, res) => {
     *       const service = new Service(req.body);
     *       await service.save();
     *       res.status(201).json(service);
     *   });
     */
    @PostMapping
    public ResponseEntity<ApplicationService> createService(@Valid @RequestBody ApplicationService service) {
        ApplicationService created = serviceService.createService(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 🔄 PUT - Mise à jour complète
     * ═══════════════════════════════════════════════════════════════════════════
     */

    /**
     * PUT /api/services/{id}
     * Met à jour un service existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationService> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationService service) {
        try {
            ApplicationService updated = serviceService.updateService(id, service);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 🔧 PATCH - Mise à jour partielle
     * ═══════════════════════════════════════════════════════════════════════════
     */

    /**
     * PATCH /api/services/{id}/status
     * Met à jour uniquement le statut d'un service
     * 
     * Body JSON attendu : { "status": "MAINTENANCE", "message": "..." }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationService> updateServiceStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            ServiceStatus status = ServiceStatus.valueOf(statusUpdate.get("status"));
            String message = statusUpdate.get("message");
            
            ApplicationService updated = serviceService.updateServiceStatus(id, status, message);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 🗑️ DELETE - Suppression
     * ═══════════════════════════════════════════════════════════════════════════
     */

    /**
     * DELETE /api/services/{id}
     * Supprime un service
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
