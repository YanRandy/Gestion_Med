package com.hopital.repository;

import com.hopital.entity.SituationMatrimoniale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import randy.framework.repository.RGenericRepository;

import java.util.Optional;

@Repository
public class SituationMatrimonialeRepository extends RGenericRepository<SituationMatrimoniale> {

    public SituationMatrimonialeRepository(JdbcTemplate jdbc) {
        super(jdbc, SituationMatrimoniale.class, "situation_matrimoniale");
    }

    public Optional<SituationMatrimoniale> findByLibelle(String libelle) {
        return findOneBy("libelle", libelle);
    }
}
