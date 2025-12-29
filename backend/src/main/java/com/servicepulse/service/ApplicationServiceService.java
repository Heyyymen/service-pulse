package com.servicepulse.service;

import com.servicepulse.model.ApplicationService;
import com.servicepulse.model.ServiceStatus;
import com.servicepulse.repository.ApplicationServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 🎓 SERVICE - Couche de logique métier
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 QU'EST-CE QU'UN SERVICE EN SPRING ?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Le Service est la couche qui contient la LOGIQUE MÉTIER de ton application.
 * 
 * Architecture en couches (comme en JS) :
 * 
 *   Controller (routes)  →  Service (logique)  →  Repository (données)
 *       ↓                       ↓                      ↓
 *   Express Router        Business Logic         Mongoose/Sequelize
 * 
 * Pourquoi séparer Controller et Service ?
 * - Le Controller ne fait que recevoir/renvoyer des requêtes HTTP
 * - Le Service contient les règles métier (validations, calculs, etc.)
 * - Facilite les tests unitaires
 * - Réutilisable (un service peut être appelé par plusieurs controllers)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 ANNOTATIONS IMPORTANTES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * @Service = Dit à Spring "c'est un bean de type Service"
 *            Spring va créer UNE SEULE instance (Singleton) et l'injecter où besoin
 * 
 * @RequiredArgsConstructor = Lombok génère le constructeur avec les champs "final"
 *                            C'est l'équivalent de l'injection par constructeur
 * 
 * @Transactional = Gère les transactions de base de données
 *                  Si une erreur survient, toutes les modifications sont annulées (rollback)
 */
@Service
@RequiredArgsConstructor
public class ApplicationServiceService {

    /*
     * 🎓 INJECTION DE DÉPENDANCES
     * 
     * En JavaScript, tu ferais :
     *   const repository = require('./repository');
     * 
     * En Spring, on utilise l'injection de dépendances.
     * Le mot-clé "final" + @RequiredArgsConstructor fait que Spring
     * injecte automatiquement le repository au démarrage.
     * 
     * C'est comme si Spring faisait automatiquement :
     *   new ApplicationServiceService(applicationServiceRepository)
     */
    private final ApplicationServiceRepository repository;

    /**
     * Récupère tous les services applicatifs
     */
    public List<ApplicationService> getAllServices() {
        return repository.findAll();
    }

    /**
     * Récupère un service par son ID
     * 
     * @param id L'identifiant du service
     * @return Optional contenant le service ou vide si non trouvé
     */
    public Optional<ApplicationService> getServiceById(Long id) {
        return repository.findById(id);
    }

    /**
     * Récupère un service par son nom
     */
    public Optional<ApplicationService> getServiceByName(String name) {
        return repository.findByName(name);
    }

    /**
     * Récupère les services filtrés par statut
     */
    public List<ApplicationService> getServicesByStatus(ServiceStatus status) {
        return repository.findByStatus(status);
    }

    /**
     * Récupère tous les services qui ne sont pas opérationnels
     * (utile pour le dashboard d'alertes)
     */
    public List<ApplicationService> getNonOperationalServices() {
        return repository.findByStatusNot(ServiceStatus.OPERATIONAL);
    }

    /**
     * Crée un nouveau service
     * 
     * @Transactional = Si une erreur survient, l'insertion est annulée
     */
    @Transactional
    public ApplicationService createService(ApplicationService service) {
        // Vérifier si un service avec ce nom existe déjà
        if (repository.existsByName(service.getName())) {
            throw new IllegalArgumentException("Un service avec ce nom existe déjà: " + service.getName());
        }
        return repository.save(service);
    }

    /**
     * Met à jour un service existant
     */
    @Transactional
    public ApplicationService updateService(Long id, ApplicationService updatedService) {
        return repository.findById(id)
                .map(existingService -> {
                    // Mettre à jour les champs
                    existingService.setName(updatedService.getName());
                    existingService.setDescription(updatedService.getDescription());
                    existingService.setStatus(updatedService.getStatus());
                    existingService.setMaintenanceMessage(updatedService.getMaintenanceMessage());
                    return repository.save(existingService);
                })
                .orElseThrow(() -> new IllegalArgumentException("Service non trouvé avec l'ID: " + id));
    }

    /**
     * Met à jour uniquement le statut d'un service
     * (endpoint pratique pour le dashboard admin)
     */
    @Transactional
    public ApplicationService updateServiceStatus(Long id, ServiceStatus status, String maintenanceMessage) {
        return repository.findById(id)
                .map(service -> {
                    service.setStatus(status);
                    service.setMaintenanceMessage(maintenanceMessage);
                    return repository.save(service);
                })
                .orElseThrow(() -> new IllegalArgumentException("Service non trouvé avec l'ID: " + id));
    }

    /**
     * Supprime un service
     */
    @Transactional
    public void deleteService(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Service non trouvé avec l'ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Recherche des services par nom (partiel)
     */
    public List<ApplicationService> searchServices(String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }

    /**
     * Compte le nombre total de services
     */
    public long countServices() {
        return repository.count();
    }

    /**
     * Vérifie si tous les services sont opérationnels
     */
    public boolean areAllServicesOperational() {
        return repository.findByStatusNot(ServiceStatus.OPERATIONAL).isEmpty();
    }

}
