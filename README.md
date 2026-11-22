# Gestion_Med

Application web de gestion hospitalière : patients, état civil, personnel et rôles (admin, médecin, infirmier, secrétaire).

Stack : Randy Framework, Spring (IoC, JDBC), PostgreSQL.

## Prérequis

- JDK 21
- Maven 3.8+
- Tomcat 10.1+
- PostgreSQL 14+
- Randy Framework installé dans le dépôt Maven local

## Installation

### Base de données

```bash
psql -U postgres -c "CREATE DATABASE hopital;"
psql -U postgres -d hopital -f sql/hopital.sql
```

### Configuration

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Renseigner `db.url`, `db.username` et `db.password` dans `db.properties`.

### Déploiement

```bash
TOMCAT_WEBAPPS=/chemin/vers/tomcat/webapps bash deploy.sh
```

URL : http://localhost:8080/hopital/
