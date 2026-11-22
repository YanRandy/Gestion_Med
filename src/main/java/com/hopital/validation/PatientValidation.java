package com.hopital.validation;

import com.hopital.entity.Patient;
import com.hopital.repository.NationaliteRepository;
import com.hopital.repository.PatientRepository;
import com.hopital.repository.SituationMatrimonialeRepository;
import org.springframework.stereotype.Component;

/**
 * Vérifications des formulaires de l'entité Patient : une méthode par formulaire.
 *  - verifierCreation      : formulaire « Nouveau patient »
 *  - verifierModification  : formulaire « Modifier la fiche »
 *  - verifierSuppression   : bouton « Supprimer le patient »
 */
@Component
public class PatientValidation {

    private final PatientRepository patientRepository;
    private final NationaliteRepository nationaliteRepository;
    private final SituationMatrimonialeRepository situationRepository;

    public PatientValidation(PatientRepository patientRepository,
                             NationaliteRepository nationaliteRepository,
                             SituationMatrimonialeRepository situationRepository) {
        this.patientRepository = patientRepository;
        this.nationaliteRepository = nationaliteRepository;
        this.situationRepository = situationRepository;
    }

    /** Formulaire « Nouveau patient ». */
    public void verifierCreation(Patient patient) {
        Verification v = verifierChamps(patient);
        v.si(patient.getNumCin() != null && patientRepository.findByNumCin(patient.getNumCin()).isPresent(),
                "Ce numéro de CIN est déjà enregistré.");
        v.lancer();
    }

    /** Formulaire « Modifier la fiche ». */
    public void verifierModification(Patient patient) {
        if (patient.getId() == null) {
            throw new IllegalArgumentException("Paramètre id manquant.");
        }
        if (patientRepository.findById(patient.getId()).isEmpty()) {
            throw new IllegalArgumentException("Patient introuvable.");
        }
        Verification v = verifierChamps(patient);
        v.si(patient.getNumCin() != null && patientRepository.findByNumCin(patient.getNumCin())
                        .filter(autre -> !autre.getId().equals(patient.getId()))
                        .isPresent(),
                "Ce numéro de CIN est déjà enregistré pour un autre patient.");
        v.lancer();
    }

    /** Bouton « Supprimer le patient ». */
    public void verifierSuppression(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Paramètre id manquant.");
        }
        if (patientRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Patient introuvable.");
        }
    }

    /** Règles communes à la création et à la modification (tailles = colonnes de la table patient). */
    private Verification verifierChamps(Patient p) {
        return new Verification()
                // Identité
                .obligatoire(p.getNom(), "Le nom")
                .longueurMax(p.getNom(), 100, "Le nom")
                .longueurMax(p.getPrenoms(), 150, "Les prénoms")
                .longueurMax(p.getNomUsage(), 100, "Le nom d'usage")
                .si(!"M".equals(p.getSexe()) && !"F".equals(p.getSexe()), "Le sexe doit être Masculin ou Féminin.")
                // Naissance
                .obligatoire(p.getDateNaissance(), "La date de naissance")
                .pasDansLeFutur(p.getDateNaissance(), "La date de naissance")
                .apres(p.getDateNaissance(), "1900-01-01", "La date de naissance doit être après 1900.")
                .longueurMax(p.getLieuNaissance(), 100, "Le lieu de naissance")
                // Filiation
                .longueurMax(p.getNomPere(), 100, "Le nom du père")
                .longueurMax(p.getPrenomsPere(), 150, "Les prénoms du père")
                .longueurMax(p.getNomMere(), 100, "Le nom de la mère")
                .longueurMax(p.getPrenomsMere(), 150, "Les prénoms de la mère")
                // Situation
                .si(p.getIdSituationMatrimoniale() != null
                        && situationRepository.findById(p.getIdSituationMatrimoniale()).isEmpty(),
                        "La situation matrimoniale choisie n'existe pas.")
                .si(p.getIdNationalite() != null
                        && nationaliteRepository.findById(p.getIdNationalite()).isEmpty(),
                        "La nationalité choisie n'existe pas.")
                // Coordonnées
                .longueurMax(p.getAdresse(), 255, "L'adresse")
                .telephone(p.getTelephone(), "Le téléphone")
                .email(p.getEmail(), "L'email")
                .longueurMax(p.getEmail(), 100, "L'email")
                .longueurMax(p.getContactUrgenceNom(), 150, "Le contact d'urgence")
                .telephone(p.getContactUrgenceTel(), "Le téléphone du contact d'urgence")
                // Pièce d'identité
                .longueurMax(p.getNumCin(), 20, "Le numéro de CIN")
                .pasDansLeFutur(p.getDateDelivranceCin(), "La date de délivrance de la CIN")
                .apres(p.getDateDelivranceCin(), p.getDateNaissance(),
                        "La CIN ne peut pas être délivrée avant la naissance.")
                .longueurMax(p.getLieuDelivranceCin(), 100, "Le lieu de délivrance de la CIN");
    }
}
