# Projet Hôpital

Application web de gestion hospitalière (gestion des patients, de leur état civil, et du personnel avec rôles : admin, médecin, infirmier, secrétaire).
Construite sur le « Randy Framework » (front controller maison) + Spring (injection et JDBC), avec une base PostgreSQL.

## Prérequis

- Java 21 (JDK)
- Maven 3.8+
- Apache Tomcat 10.1+ (Jakarta EE / Servlet 5+)
- PostgreSQL 14+
- Randy Framework installé dans le dépôt Maven local (`mvn install` dans le projet du framework)

## Déploiement

1. **Base de données**
   ```bash
   psql -U postgres -c "CREATE DATABASE hopital;"
   psql -U postgres -d hopital -f Hopital/sql/hopital.sql
   ```

2. **Identifiants** : copier le modèle puis renseigner user / mot de passe
   ```bash
   cp Hopital/src/main/resources/db.properties.example Hopital/src/main/resources/db.properties
   ```
   `db.properties` est ignoré par git : il ne doit jamais être commité.

3. **Build + déploiement dans Tomcat**
   ```bash
   cd Hopital
   TOMCAT_WEBAPPS=/chemin/vers/tomcat/webapps bash deploy.sh
   ```
   L'application sera accessible en local sur http://localhost:8080/hopital/
