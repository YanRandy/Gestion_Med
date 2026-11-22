package com.hopital.repository;

import com.hopital.entity.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import randy.framework.repository.RGenericRepository;

import java.util.Optional;

@Repository
public class RoleRepository extends RGenericRepository<Role> {

    public RoleRepository(JdbcTemplate jdbc) {
        super(jdbc, Role.class, "roles");
    }

    public Optional<Role> findByLibelle(String libelle) {
        return findOneBy("libelle", libelle);
    }
}
