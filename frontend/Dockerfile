# ═══════════════════════════════════════════════════════════════════════════════
# 🐳 DOCKERFILE - Frontend Angular
# ═══════════════════════════════════════════════════════════════════════════════

# ─────────────────────────────────────────────────────────────────────────────
# ÉTAPE 1 : BUILD - Compiler l'application Angular
# ─────────────────────────────────────────────────────────────────────────────
FROM node:20-alpine AS build

WORKDIR /app

# Copier les fichiers de dépendances
COPY package*.json ./

# Installer toutes les dépendances (y compris devDependencies pour le build)
RUN npm ci

# Copier le code source
COPY . .

# Build en mode production
RUN npm run build -- --configuration production

# ─────────────────────────────────────────────────────────────────────────────
# ÉTAPE 2 : RUNTIME - Servir avec Nginx
# ─────────────────────────────────────────────────────────────────────────────
FROM nginx:alpine

# Copier la configuration nginx personnalisée
COPY nginx.conf /etc/nginx/nginx.conf

# Copier les fichiers buildés depuis l'étape de build
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Exposer le port 80
EXPOSE 80

# Nginx démarre automatiquement avec l'image

# Labels
LABEL maintainer="aymen"
LABEL description="Service Pulse Frontend"
LABEL version="1.0.0"
