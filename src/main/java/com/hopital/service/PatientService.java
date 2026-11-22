package com.hopital.service;

import com.hopital.entity.Patient;
import com.hopital.entity.User;
import com.hopital.repository.PatientRepository;
import com.hopital.validation.PatientValidation;
import com.hopital.validation.UserValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PatientValidation patientValidation;
    private final UserValidation userValidation;

    public PatientService(PatientRepository patientRepository, UserService userService,
                          PatientValidation patientValidation, UserValidation userValidation) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.patientValidation = patientValidation;
        this.userValidation = userValidation;
    }

    public Patient create(Patient patient) {
        patientValidation.verifierCreation(patient);
        patient.setIdUser(null);
        return patientRepository.save(patient);
    }

    /** Modifie la fiche ; l'accès patient (idUser) n'est pas touché. */
    public Patient update(Patient patient) {
        patientValidation.verifierModification(patient);
        Patient existing = patientRepository.findById(patient.getId()).orElseThrow();
        patient.setIdUser(existing.getIdUser());
        patientRepository.update(patient);
        return patientRepository.findById(patient.getId()).orElse(patient);
    }

    /** Supprime la fiche et, s'il existe, le compte d'accès du patient. */
    public void delete(Integer id) {
        patientValidation.verifierSuppression(id);
        Patient patient = patientRepository.findById(id).orElseThrow();
        patientRepository.deleteById(id);
        if (patient.getIdUser() != null) {
            userService.delete(patient.getIdUser());
        }
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(int id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> findByUser(int idUser) {
        return patientRepository.findByIdUser(idUser);
    }

    public List<Patient> search(String nom) {
        if (nom == null || nom.isBlank()) {
            return patientRepository.findAll();
        }
        return patientRepository.searchByNom(nom.trim());
    }

    public User createAccess(int idPatient, String email, String password) {
        Patient patient = patientRepository.findById(idPatient).orElse(null);
        userValidation.verifierAccesPatient(patient, email, password);
        User user = userService.create(email, email, password, UserService.ROLE_PATIENT);
        patient.setIdUser(user.getId());
        patientRepository.update(patient);
        return user;
    }
}
