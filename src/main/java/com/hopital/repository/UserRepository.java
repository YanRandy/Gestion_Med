package com.hopital.repository;

import com.hopital.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import randy.framework.repository.RGenericRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends RGenericRepository<User> {

    public UserRepository(JdbcTemplate jdbc) {
        super(jdbc, User.class, "users");
    }

    public Optional<User> findByUsername(String username) {
        return findOneBy("username", username);
    }

    public Optional<User> findByEmail(String email) {
        return findOneBy("email", email);
    }

    public List<User> findByRole(int idRole) {
        return findBy("id_role", idRole);
    }
}
