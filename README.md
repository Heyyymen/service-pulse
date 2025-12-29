# 🛠️ Service Pulse

> Portail de gestion de maintenance applicative

## 📋 Description

Application de démonstration pour la gestion du statut des services applicatifs.
Similaire à un outil eMaintenance bancaire.

## 🏗️ Architecture

```
service-pulse/
├── backend/                 # API Spring Boot
│   └── src/
│       ├── main/
│       │   ├── java/com/servicepulse/
│       │   │   ├── config/          # Configuration (Security, CORS, etc.)
│       │   │   ├── controller/      # Points d'entrée REST (comme les routes Express)
│       │   │   ├── service/         # Logique métier
│       │   │   ├── repository/      # Accès aux données (comme les DAO)
│       │   │   ├── model/           # Entités JPA (comme les modèles Mongoose)
│       │   │   ├── dto/             # Data Transfer Objects
│       │   │   ├── exception/       # Gestion des erreurs personnalisées
│       │   │   └── security/        # JWT, filtres d'authentification
│       │   └── resources/
│       │       └── application.yml  # Configuration (comme .env)
│       └── test/                    # Tests unitaires et d'intégration
│
├── frontend/                # Application Angular
│   └── src/
│       ├── app/
│       │   ├── core/               # Services singleton, guards, interceptors
│       │   ├── shared/             # Composants réutilisables
│       │   ├── features/           # Modules par fonctionnalité
│       │   │   ├── dashboard/
│       │   │   └── admin/
│       │   └── models/             # Interfaces TypeScript
│       └── environments/
│
├── docker/                  # Fichiers Docker
├── .github/workflows/       # CI/CD GitHub Actions
└── docs/                    # Documentation technique
```

## 🚀 Technologies

- **Backend**: Spring Boot 3, Java 17+
- **Frontend**: Angular 18+, TypeScript
- **Sécurité**: Spring Security, JWT, OAuth2
- **Base de données**: H2 (dev), PostgreSQL (prod)
- **DevOps**: Docker, GitHub Actions, Spring Actuator

## 👥 Profils Utilisateurs

| Profil | Droits |
|--------|--------|
| LECTEUR | Consulter les statuts des services |
| ADMIN | Modifier les statuts et messages de maintenance |

## 📝 License

Projet de stage - Usage éducatif
