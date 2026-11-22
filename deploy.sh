#!/bin/bash
# Build + déploiement de Hopital dans Tomcat
# Usage : bash deploy.sh   (depuis le dossier Hopital)

set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
TOMCAT_WEBAPPS="${TOMCAT_WEBAPPS:-/home/randylam/Documents/tomcat/Tomcat/webapps}"   # surchargeable : TOMCAT_WEBAPPS=/chemin/webapps bash deploy.sh
APP_NAME="hopital"                       # → http://localhost:8080/hopital/
WAR_FILE="$PROJECT_DIR/target/${APP_NAME}.war"

# Le projet est en Java 21 (le java par défaut est le 8)
export JAVA_HOME="${JAVA_HOME_21:-/usr/lib/jvm/java-21-openjdk-amd64}"

cd "$PROJECT_DIR"
mvn clean package -DskipTests -q
echo "Build OK : $WAR_FILE"

rm -f  "$TOMCAT_WEBAPPS/${APP_NAME}.war"
rm -rf "$TOMCAT_WEBAPPS/${APP_NAME}"
cp "$WAR_FILE" "$TOMCAT_WEBAPPS/${APP_NAME}.war"
echo "WAR copié dans $TOMCAT_WEBAPPS"

# Démarrage de Tomcat s'il ne tourne pas déjà (sinon l'autodeploy recharge le WAR)
TOMCAT_HOME="$(dirname "$TOMCAT_WEBAPPS")"
if pgrep -f "catalina.base=$TOMCAT_HOME" > /dev/null; then
    echo "Tomcat déjà démarré : rechargement automatique du WAR"
else
    echo "Démarrage de Tomcat..."
    "$TOMCAT_HOME/bin/catalina.sh" start
fi

echo "Déployé : http://localhost:8080/${APP_NAME}/"
