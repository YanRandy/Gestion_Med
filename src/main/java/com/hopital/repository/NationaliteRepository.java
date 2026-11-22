package com.hopital.repository;

import com.hopital.entity.Nationalite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import randy.framework.repository.RGenericRepository;

import java.util.Optional;

@Repository
public class NationaliteRepository extends RGenericRepository<Nationalite> {

    public NationaliteRepository(JdbcTemplate jdbc) {
        super(jdbc, Nationalite.class, "nationalite");
    }

    public Optional<Nationalite> findByLibelle(String libelle) {
        return findOneBy("libelle", libelle);
    }
}
