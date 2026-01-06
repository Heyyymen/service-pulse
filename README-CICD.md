# 🔄 Guide CI/CD - Service Pulse

## 📋 Workflows GitHub Actions

### 1. **CI/CD Pipeline** (`ci-cd.yml`)
Pipeline principal qui s'exécute sur chaque push et pull request.

**Étapes :**
- ✅ Build Backend (Maven)
- ✅ Tests Backend
- ✅ Build Frontend (npm)
- ✅ Tests Frontend
- ✅ Build Docker Images
- ✅ Security Scan (Trivy)
- ✅ Déploiement (optionnel)

**Déclenchement :**
```bash
git push origin main
```

### 2. **Code Quality** (`code-quality.yml`)
Vérification de la qualité du code sur les Pull Requests.

**Vérifie :**
- Style de code Java (Checkstyle)
- Lint Angular (ESLint)
- Commentaires automatiques sur PR

### 3. **Release** (`release.yml`)
Création automatique de releases avec tags.

**Utilisation :**
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### 4. **Dependency Update** (`dependency-update.yml`)
Mise à jour automatique des dépendances chaque lundi.

**Crée automatiquement :**
- PR avec les dernières versions de dépendances
- Changelog des modifications

---

## 🔐 Secrets à configurer

Dans GitHub : **Settings → Secrets and variables → Actions**

### Optionnels (Docker Hub) :
- `DOCKER_USERNAME` : Votre nom d'utilisateur Docker Hub
- `DOCKER_PASSWORD` : Votre token Docker Hub

### Déploiement (si nécessaire) :
- `SERVER_HOST` : IP du serveur
- `SERVER_USER` : Utilisateur SSH
- `SSH_PRIVATE_KEY` : Clé privée SSH

---

## 📊 Badges pour README

Ajoute ces badges dans ton `README.md` :

```markdown
![CI/CD](https://github.com/VOTRE_USERNAME/service-pulse/actions/workflows/ci-cd.yml/badge.svg)
![Code Quality](https://github.com/VOTRE_USERNAME/service-pulse/actions/workflows/code-quality.yml/badge.svg)
![Release](https://github.com/VOTRE_USERNAME/service-pulse/actions/workflows/release.yml/badge.svg)
```

---

## 🚀 Déploiement automatique

### Option 1 : Déploiement via SSH

Décommente dans `ci-cd.yml` :
```yaml
- name: Deploy via SSH
  uses: appleboy/ssh-action@v1.0.0
  with:
    host: ${{ secrets.SERVER_HOST }}
    username: ${{ secrets.SERVER_USER }}
    key: ${{ secrets.SSH_PRIVATE_KEY }}
    script: |
      cd /opt/service-pulse
      docker-compose pull
      docker-compose up -d
```

### Option 2 : Kubernetes

```yaml
- name: Deploy to Kubernetes
  run: |
    kubectl set image deployment/backend backend=service-pulse-backend:${{ github.sha }}
    kubectl set image deployment/frontend frontend=service-pulse-frontend:${{ github.sha }}
```

### Option 3 : Azure/AWS/GCP

Utilise les actions officielles :
- Azure: `azure/webapps-deploy@v2`
- AWS: `aws-actions/configure-aws-credentials@v4`
- GCP: `google-github-actions/setup-gcloud@v2`

---

## 🔍 Monitoring du Pipeline

### Voir les logs
1. Va sur GitHub → Actions
2. Clique sur le workflow
3. Sélectionne le job pour voir les logs détaillés

### Notifications
Configure des notifications dans :
- Slack
- Discord
- Email
- Microsoft Teams

Exemple Slack :
```yaml
- name: Slack Notification
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'Build ${{ job.status }}'
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

---

## 🧪 Tests locaux

Avant de push, teste localement :

### Backend
```bash
cd backend
mvn clean test
mvn package -DskipTests
```

### Frontend
```bash
cd frontend
npm run lint
npm run test
npm run build
```

### Docker
```bash
docker-compose up --build
```

---

## 📈 Métriques et Rapports

Le pipeline génère automatiquement :
- ✅ Coverage reports (JaCoCo pour Java)
- ✅ Test reports
- ✅ Security scan reports (Trivy)
- ✅ Build artifacts

Accès via **Actions → Artifacts**

---

## 🎯 Bonnes pratiques

1. **Protéger la branche main**
   - Settings → Branches → Add rule
   - Require pull request reviews
   - Require status checks to pass

2. **Conventions de commit**
   ```
   feat: nouvelle fonctionnalité
   fix: correction de bug
   docs: documentation
   style: formatage
   refactor: refactorisation
   test: ajout de tests
   chore: tâches de maintenance
   ```

3. **Versioning sémantique**
   - v1.0.0 : Release majeure
   - v1.1.0 : Nouvelles fonctionnalités
   - v1.1.1 : Corrections de bugs

---

## 🆘 Troubleshooting

### Build échoue
```bash
# Vérifier localement
./mvnw clean install
npm run build
```

### Tests échouent
```bash
# Exécuter les tests localement
./mvnw test
npm run test
```

### Docker build échoue
```bash
# Nettoyer le cache Docker
docker system prune -a
docker-compose build --no-cache
```

---

## 📚 Ressources

- [GitHub Actions Docs](https://docs.github.com/actions)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Maven CI/CD](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
