-- Base Hopital (PostgreSQL)
-- Création de la base (une seule fois) : CREATE DATABASE hopital;
-- Puis : psql -U postgres -d hopital -f sql/hopital.sql

-- ============================================================
--  Tables de référence
-- ============================================================

CREATE TABLE situation_matrimoniale (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE nationalite (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- ============================================================
--  Utilisateurs (personnel qui se connecte)
-- ============================================================

CREATE TABLE roles (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- "user" est un mot réservé en PostgreSQL → users
CREATE TABLE users (
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    id_role    INT          NOT NULL REFERENCES roles(id),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
--  Patient + état civil
-- ============================================================

CREATE TABLE patient (
    id                        SERIAL PRIMARY KEY,

    -- Identité complète
    nom                       VARCHAR(100) NOT NULL,
    prenoms                   VARCHAR(150),
    nom_usage                 VARCHAR(100),

    -- Naissance
    date_naissance            DATE         NOT NULL,
    est_date_presumee         BOOLEAN      NOT NULL DEFAULT FALSE,
    lieu_naissance            VARCHAR(100),

    -- Sexe
    sexe                      CHAR(1)      NOT NULL CHECK (sexe IN ('M', 'F')),

    -- Filiation (nullable : parent inconnu possible)
    nom_pere                  VARCHAR(100),
    prenoms_pere              VARCHAR(150),
    nom_mere                  VARCHAR(100),
    prenoms_mere              VARCHAR(150),

    -- Situation matrimoniale
    id_situation_matrimoniale INT REFERENCES situation_matrimoniale(id),

    -- Coordonnées
    adresse                   VARCHAR(255),
    telephone                 VARCHAR(20),
    email                     VARCHAR(100),
    contact_urgence_nom       VARCHAR(150),
    contact_urgence_tel       VARCHAR(20),

    -- Nationalité
    id_nationalite            INT REFERENCES nationalite(id),

    -- Pièce d'identité
    num_cin                   VARCHAR(20) UNIQUE,
    date_delivrance_cin       DATE,
    lieu_delivrance_cin       VARCHAR(100),

    -- Accès patient (compte créé par le médecin, NULL tant qu'il n'existe pas)
    id_user                   INT UNIQUE REFERENCES users(id),

    created_at                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
--  Données de référence
-- ============================================================

INSERT INTO situation_matrimoniale (libelle) VALUES
    ('Célibataire'), ('Marié(e)'), ('Divorcé(e)'), ('Veuf(ve)'), ('Union libre');

INSERT INTO nationalite (libelle) VALUES
    ('Malagasy'), ('Française'), ('Comorienne'), ('Mauricienne'), ('Autre');

INSERT INTO roles (libelle) VALUES
    ('ADMIN'), ('MEDECIN'), ('INFIRMIER'), ('SECRETAIRE'), ('PATIENT');
