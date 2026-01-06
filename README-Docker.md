# 🐳 Guide Docker - Service Pulse

## Prérequis

- Docker Desktop installé et démarré
- Git Bash ou PowerShell

## 🚀 Démarrage rapide

### Lancer toute l'application

```bash
docker-compose up -d
```

L'application sera accessible sur :
- **Frontend** : http://localhost
- **Backend API** : http://localhost:8080
- **H2 Console** : http://localhost:8080/h2-console
- **Actuator** : http://localhost:8080/actuator/health

### Arrêter l'application

```bash
docker-compose down
```

### Reconstruire les images après modifications

```bash
docker-compose up -d --build
```

## 📦 Commandes utiles

### Voir les logs

```bash
# Tous les services
docker-compose logs -f

# Backend uniquement
docker-compose logs -f backend

# Frontend uniquement
docker-compose logs -f frontend
```

### Vérifier le statut

```bash
docker-compose ps
```

### Entrer dans un container

```bash
# Backend
docker exec -it service-pulse-backend sh

# Frontend
docker exec -it service-pulse-frontend sh
```

### Nettoyer tout

```bash
# Arrêter et supprimer les containers
docker-compose down

# Supprimer aussi les volumes et images
docker-compose down -v --rmi all
```

## 🔧 Build individuel

### Backend seulement

```bash
cd backend
docker build -t service-pulse-backend .
docker run -p 8080:8080 service-pulse-backend
```

### Frontend seulement

```bash
cd frontend
docker build -t service-pulse-frontend .
docker run -p 80:80 service-pulse-frontend
```

## 📊 Monitoring

### Statistiques en temps réel

```bash
docker stats
```

### Inspecter un container

```bash
docker inspect service-pulse-backend
```

## 🐛 Troubleshooting

### Le backend ne démarre pas

```bash
docker-compose logs backend
```

### Le frontend ne se connecte pas au backend

Vérifie que l'URL dans `environment.ts` pointe bien vers `http://localhost:8080`

### Port déjà utilisé

```bash
# Changer les ports dans docker-compose.yml
ports:
  - "8081:8080"  # Backend sur 8081
  - "4200:80"    # Frontend sur 4200
```

## 📝 Notes importantes

- Les images utilisent des builds multi-stage pour optimiser la taille
- Le backend utilise Java 17 JRE Alpine (léger)
- Le frontend est servi par Nginx Alpine (très léger)
- Les healthchecks garantissent que le frontend démarre après le backend
- Tous les containers redémarrent automatiquement sauf si arrêtés manuellement

## 🎯 Prochaines étapes

1. Ajouter une base de données PostgreSQL au docker-compose
2. Utiliser des volumes pour persister les données
3. Configurer un reverse proxy Nginx
4. Ajouter du monitoring avec Prometheus/Grafana
