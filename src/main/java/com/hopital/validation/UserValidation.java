package com.hopital.validation;

import com.hopital.entity.Patient;
import com.hopital.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Vérifications des formulaires de l'entité User : une méthode par formulaire.
 *  - verifierConnexion    : formulaire de connexion (/login)
 *  - verifierCreation     : création d'un compte (règles communes)
 *  - verifierAccesPatient : formulaire « Créer l'accès patient » (fiche patient)
 */
@Component
public class UserValidation {

    public static final int PASSWORD_MIN_LENGTH = 8;

    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Formulaire de connexion. */
    public void verifierConnexion(String email, String password) {
        new Verification()
                .obligatoire(email, "L'adresse email")
                .obligatoire(password, "Le mot de passe")
                .lancer();
    }

    /** Création d'un compte (personnel ou patient). */
    public void verifierCreation(String username, String email, String password) {
        Verification v = new Verification()
                .obligatoire(username, "Le nom d'utilisateur")
                .longueurMax(username, 50, "Le nom d'utilisateur")
                .obligatoire(email, "L'adresse email")
                .email(email, "L'adresse email")
                .longueurMax(email, 100, "L'adresse email")
                .obligatoire(password, "Le mot de passe")
                .longueurMin(password, PASSWORD_MIN_LENGTH, "Le mot de passe");
        if (!v.aDesErreurs()) {
            v.si(userRepository.findByUsername(username.trim()).isPresent(), "Ce nom d'utilisateur est déjà utilisé.")
             .si(userRepository.findByEmail(email.trim()).isPresent(), "Cette adresse email est déjà utilisée.");
        }
        v.lancer();
    }

    /** Formulaire « Créer l'accès patient ». */
    public void verifierAccesPatient(Patient patient, String email, String password) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient introuvable.");
        }
        if (patient.getIdUser() != null) {
            throw new IllegalArgumentException("Ce patient a déjà un accès.");
        }
        // L'email sert aussi de nom d'utilisateur pour un patient
        verifierCreation(email, email, password);
    }
}
