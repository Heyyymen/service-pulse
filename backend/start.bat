@echo off
REM ═══════════════════════════════════════════════════════════════════════════════
REM 🚀 Script de démarrage Service Pulse Backend
REM ═══════════════════════════════════════════════════════════════════════════════

echo.
echo  ╔═══════════════════════════════════════════════════════════════╗
echo  ║              SERVICE PULSE - Backend Startup                  ║
echo  ╚═══════════════════════════════════════════════════════════════╝
echo.

REM Configuration de l'environnement
set JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.17.10-hotspot
set PATH=%PATH%;C:\tools\apache-maven-3.9.6\bin

echo [INFO] JAVA_HOME = %JAVA_HOME%
echo [INFO] Starting Spring Boot application...
echo.
echo [INFO] Endpoints disponibles:
echo        - Application:    http://localhost:8080
echo        - H2 Console:     http://localhost:8080/h2-console
echo        - Health Check:   http://localhost:8080/actuator/health
echo.
echo [INFO] Appuyez sur Ctrl+C pour arrêter le serveur
echo.

cd /d "%~dp0"
mvn spring-boot:run
