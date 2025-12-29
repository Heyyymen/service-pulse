-- ═══════════════════════════════════════════════════════════════════════════════
-- DONNEES INITIALES POUR LE DEVELOPPEMENT
-- Ce fichier est execute automatiquement par Spring Boot au demarrage
-- ═══════════════════════════════════════════════════════════════════════════════

INSERT INTO application_services (name, description, status, maintenance_message, created_at, last_updated) VALUES
('API Paiements', 'Service de traitement des paiements en ligne', 'OPERATIONAL', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Authentification ADFS', 'Service d''authentification Active Directory', 'OPERATIONAL', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Base de donnees Clients', 'Base PostgreSQL des donnees clients', 'DEGRADED', 'Performances reduites - Investigation en cours', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Service Email', 'Envoi des notifications par email', 'MAINTENANCE', 'Maintenance planifiee jusqu''a 14h00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Gateway Mobile', 'API Gateway pour l''application mobile', 'OPERATIONAL', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Service de Cache', 'Cache Redis pour les sessions', 'OUTAGE', 'Incident en cours - Equipe mobilisee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
