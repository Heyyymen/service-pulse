package com.servicepulse.model;

/**
 * 🎓 ENUM - ServiceStatus
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📚 QU'EST-CE QU'UNE ENUM ?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Une Enum est un type qui ne peut avoir que des valeurs prédéfinies.
 * 
 * Analogie JavaScript:
 * 
 *   // En JS, tu ferais un objet constant:
 *   const ServiceStatus = Object.freeze({
 *       OPERATIONAL: 'OPERATIONAL',
 *       DEGRADED: 'DEGRADED',
 *       MAINTENANCE: 'MAINTENANCE',
 *       OUTAGE: 'OUTAGE'
 *   });
 * 
 *   // Ou en TypeScript:
 *   enum ServiceStatus {
 *       OPERATIONAL = 'OPERATIONAL',
 *       DEGRADED = 'DEGRADED',
 *       MAINTENANCE = 'MAINTENANCE',
 *       OUTAGE = 'OUTAGE'
 *   }
 * 
 * L'avantage de l'Enum Java:
 * - Le compilateur vérifie que tu n'utilises que des valeurs valides
 * - Impossible de faire une faute de frappe (contrairement à une string)
 * - Auto-complétion dans l'IDE
 */
public enum ServiceStatus {
    
    /**
     * 🟢 Le service fonctionne normalement
     */
    OPERATIONAL,
    
    /**
     * 🟡 Le service fonctionne mais avec des performances réduites
     */
    DEGRADED,
    
    /**
     * 🔵 Le service est en maintenance planifiée
     */
    MAINTENANCE,
    
    /**
     * 🔴 Le service est en panne
     */
    OUTAGE
    
}
