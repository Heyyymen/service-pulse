-- Utilisateurs de test
-- Mot de passe: "password" pour tous (encodé en BCrypt)

INSERT INTO users (username, password, full_name, enabled) VALUES
('admin', '$2a$10$xJ3kIyEOcwzOgp7XzR9EkeZWPvuOlZWG5fWHbRQGZpQGxPKZ9KH5.', 'Admin User', true),
('lecteur', '$2a$10$xJ3kIyEOcwzOgp7XzR9EkeZWPvuOlZWG5fWHbRQGZpQGxPKZ9KH5.', 'Lecteur User', true);

INSERT INTO user_roles (user_id, role) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_LECTEUR'),
(2, 'ROLE_LECTEUR');
