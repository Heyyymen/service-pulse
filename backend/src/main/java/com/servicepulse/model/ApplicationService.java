package com.servicepulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 🎓 ENTITÉ JPA - ApplicationService
 * 
 * Représente un service applicatif dont on surveille le statut.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 ANALOGIE JAVASCRIPT - Mongoose Schema
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * En JavaScript avec Mongoose, tu écrirais:
 * 
 *   const serviceSchema = new mongoose.Schema({
 *       name: { type: String, required: true },
 *       description: String,
 *       status: { type: String, enum: ['OPERATIONAL', 'DEGRADED', 'MAINTENANCE', 'OUTAGE'] },
 *       maintenanceMessage: String,
 *       lastUpdated: Date
 *   });
 *   
 *   const Service = mongoose.model('Service', serviceSchema);
 * 
 * En Java avec JPA, on utilise des ANNOTATIONS pour décrire la même chose.
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 QU'EST-CE QU'UNE ANNOTATION EN JAVA ?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Une annotation commence par @ et donne des MÉTADONNÉES à la classe/méthode.
 * 
 * C'est comme les décorateurs en TypeScript:
 *   @Component({ selector: 'app-root' })  ← En Angular
 *   @Entity                                ← En Java
 * 
 * Les annotations ne changent PAS le code, elles donnent des INSTRUCTIONS
 * au framework (Spring, Hibernate) sur comment traiter cette classe.
 */

@Entity  // 📌 Dit à JPA: "Cette classe représente une table en base de données"
@Table(name = "application_services")  // 📌 Nom de la table SQL

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 ANNOTATIONS LOMBOK - Génération automatique de code
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * En Java, contrairement à JavaScript, on doit écrire des getters/setters.
 * Lombok les génère automatiquement à la compilation.
 * 
 * @Data = génère: getters + setters + toString() + equals() + hashCode()
 * @Builder = permet de créer des objets avec le pattern Builder
 * @NoArgsConstructor = constructeur vide: new ApplicationService()
 * @AllArgsConstructor = constructeur avec tous les champs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationService {

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 🔑 CLÉ PRIMAIRE
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * @Id = Cette colonne est la clé primaire (comme _id dans MongoDB)
     * @GeneratedValue = La valeur est générée automatiquement
     * IDENTITY = Auto-increment (1, 2, 3, ...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 📝 CHAMPS AVEC VALIDATION
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * @Column = Configuration de la colonne SQL
     * @NotBlank = Ne peut pas être null ou vide (comme required: true en Mongoose)
     * @Size = Limite la taille (comme maxlength en Mongoose)
     */
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le nom du service est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 🚦 STATUT DU SERVICE - Utilisation d'une Enum
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * @Enumerated(EnumType.STRING) = Stocke le nom de l'enum en texte
     * Exemple: "OPERATIONAL" sera stocké tel quel dans la colonne
     * 
     * Sans STRING, ce serait stocké comme un nombre (0, 1, 2...) - moins lisible
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status = ServiceStatus.OPERATIONAL;

    @Column(length = 1000)
    private String maintenanceMessage;

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 📅 DATES AUTOMATIQUES
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * LocalDateTime = Type Java pour date + heure (comme new Date() en JS)
     */
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /*
     * ═══════════════════════════════════════════════════════════════════════════
     * 🔄 CALLBACKS JPA - Exécutés automatiquement
     * ═══════════════════════════════════════════════════════════════════════════
     * 
     * @PrePersist = Exécuté AVANT l'insertion en base (comme un middleware Mongoose)
     * @PreUpdate = Exécuté AVANT une mise à jour
     * 
     * En Mongoose, c'est comme:
     *   schema.pre('save', function(next) {
     *       this.createdAt = new Date();
     *       next();
     *   });
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

}
