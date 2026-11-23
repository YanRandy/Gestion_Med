-- ============================================================
--  Comptes de test (à lancer après hopital.sql)
--  psql -U postgres -d hopital -f sql/donnees_test.sql
--
--  Médecin 1 : medecin1@hopital.mg / medecin123
--  Médecin 2 : medecin2@hopital.mg / medecin123
--  Patient   : patient@hopital.mg  / patient123
--  (mots de passe hachés en BCrypt, comme dans UserService)
-- ============================================================

INSERT INTO users (username, email, password, id_role) VALUES
    ('medecin1@hopital.mg', 'medecin1@hopital.mg',
     '$2a$10$DGSfNDW1Q1FRE2ZLJPs.e.3lR2wXZ6K.SvdJ7PIcWjvdtxU9YZCTy',
     (SELECT id FROM roles WHERE libelle = 'MEDECIN')),
    ('medecin2@hopital.mg', 'medecin2@hopital.mg',
     '$2a$10$DGSfNDW1Q1FRE2ZLJPs.e.3lR2wXZ6K.SvdJ7PIcWjvdtxU9YZCTy',
     (SELECT id FROM roles WHERE libelle = 'MEDECIN')),
    ('patient@hopital.mg', 'patient@hopital.mg',
     '$2a$10$4gDaH5OlDJ1UJcggWDz/zuL8CG3hP21ltMJQsMSypNg56SnZD0zrW',
     (SELECT id FROM roles WHERE libelle = 'PATIENT'))
ON CONFLICT (email) DO NOTHING;

-- Fiche du patient de test, reliée à son compte
INSERT INTO patient (nom, prenoms, date_naissance, sexe, email,
                     id_situation_matrimoniale, id_nationalite, id_user)
SELECT 'Rakoto', 'Jean', DATE '1990-05-14', 'M', 'patient@hopital.mg',
       (SELECT id FROM situation_matrimoniale WHERE libelle = 'Célibataire'),
       (SELECT id FROM nationalite WHERE libelle = 'Malagasy'),
       (SELECT id FROM users WHERE email = 'patient@hopital.mg')
WHERE NOT EXISTS (SELECT 1 FROM patient
                  WHERE id_user = (SELECT id FROM users WHERE email = 'patient@hopital.mg'));
