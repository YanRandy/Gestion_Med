package com.hopital.repository;

import com.hopital.entity.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import randy.framework.repository.RGenericRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository extends RGenericRepository<Patient> {

    public PatientRepository(JdbcTemplate jdbc) {
        super(jdbc, Patient.class, "patient");
    }

    @Override
    public List<Patient> findAll() {
        return jdbc.query("SELECT * FROM patient ORDER BY nom, prenoms", mapper);
    }

    public Optional<Patient> findByNumCin(String numCin) {
        return findOneBy("num_cin", numCin);
    }

    public Optional<Patient> findByIdUser(int idUser) {
        return findOneBy("id_user", idUser);
    }

    public List<Patient> searchByNom(String nom) {
        String pattern = "%" + nom + "%";
        return jdbc.query(
                "SELECT * FROM patient WHERE nom ILIKE ? OR prenoms ILIKE ? OR nom_usage ILIKE ? ORDER BY nom, prenoms",
                mapper, pattern, pattern, pattern);
    }
}
