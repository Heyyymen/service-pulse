# ═══════════════════════════════════════════════════════════════════════════════
# 🐳 DOCKERFILE - Backend Spring Boot
# ═══════════════════════════════════════════════════════════════════════════════

# ─────────────────────────────────────────────────────────────────────────────
# ÉTAPE 1 : BUILD - Compiler l'application avec Maven
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copier d'abord les fichiers de config Maven pour profiter du cache Docker
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Télécharger les dépendances (cette couche sera cachée si pom.xml ne change pas)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler l'application (skip tests pour accélérer le build)
RUN mvn clean package -DskipTests

# ─────────────────────────────────────────────────────────────────────────────
# ÉTAPE 2 : RUNTIME - Créer l'image finale légère
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copier le JAR compilé depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Variables d'environnement par défaut (peuvent être overridées)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Commande de démarrage avec optimisations JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

# Labels pour la documentation
LABEL maintainer="aymen"
LABEL description="Service Pulse Backend API"
LABEL version="1.0.0"
