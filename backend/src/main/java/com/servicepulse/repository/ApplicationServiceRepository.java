package com.servicepulse.repository;

import com.servicepulse.model.ApplicationService;
import com.servicepulse.model.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 🎓 REPOSITORY - Accès aux données
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 QU'EST-CE QU'UN REPOSITORY ?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Le Repository est la couche qui communique avec la base de données.
 * 
 * Analogie JavaScript avec Mongoose:
 * 
 *   // En Mongoose, le modèle a des méthodes intégrées:
 *   Service.find()
 *   Service.findById(id)
 *   Service.create(data)
 *   Service.findByIdAndUpdate(id, data)
 *   Service.findByIdAndDelete(id)
 * 
 * En Spring Data JPA, on crée une INTERFACE (pas une classe!) qui HÉRITE
 * de JpaRepository, et Spring GÉNÈRE automatiquement l'implémentation!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 MAGIE DE SPRING DATA JPA
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * JpaRepository<ApplicationService, Long> signifie:
 * - ApplicationService = le type d'entité gérée
 * - Long = le type de la clé primaire (id)
 * 
 * Tu obtiens GRATUITEMENT ces méthodes:
 * - findAll()           → SELECT * FROM application_services
 * - findById(id)        → SELECT * WHERE id = ?
 * - save(entity)        → INSERT ou UPDATE automatique
 * - deleteById(id)      → DELETE WHERE id = ?
 * - count()             → SELECT COUNT(*)
 * - existsById(id)      → Vérifie si l'ID existe
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 QUERY METHODS - La vraie magie!
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Tu peux créer des requêtes JUSTE EN NOMMANT LA MÉTHODE!
 * Spring analyse le nom et génère la requête SQL.
 * 
 * Exemple:
 *   findByStatus(status) → SELECT * WHERE status = ?
 *   findByNameContaining(text) → SELECT * WHERE name LIKE '%text%'
 */

@Repository  // 📌 Dit à Spring: "C'est un composant de type Repository"
public interface ApplicationServiceRepository extends JpaRepository<ApplicationService, Long> {

    /**
     * Trouve tous les services ayant un statut spécifique
     * 
     * Spring génère automatiquement:
     * SELECT * FROM application_services WHERE status = ?
     */
    List<ApplicationService> findByStatus(ServiceStatus status);

    /**
     * Trouve un service par son nom (retourne Optional car peut ne pas exister)
     * 
     * Optional = conteneur qui peut être vide ou contenir une valeur
     * Évite les NullPointerException
     * 
     * Analogie JS: C'est comme retourner null ou undefined, mais de façon plus sûre
     */
    Optional<ApplicationService> findByName(String name);

    /**
     * Vérifie si un service avec ce nom existe déjà
     */
    boolean existsByName(String name);

    /**
     * Trouve les services dont le nom contient une chaîne (recherche)
     * IgnoreCase = insensible à la casse
     */
    List<ApplicationService> findByNameContainingIgnoreCase(String name);

    /**
     * Trouve tous les services qui ne sont PAS opérationnels
     * (utile pour le dashboard d'alertes)
     */
    List<ApplicationService> findByStatusNot(ServiceStatus status);

}
