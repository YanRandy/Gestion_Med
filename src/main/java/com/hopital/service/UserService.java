package com.hopital.service;

import com.hopital.entity.Role;
import com.hopital.entity.User;
import com.hopital.repository.RoleRepository;
import com.hopital.repository.UserRepository;
import com.hopital.validation.UserValidation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public static final String ROLE_PATIENT = "PATIENT";
    public static final String ROLE_MEDECIN = "MEDECIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidation userValidation;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userValidation = userValidation;
    }

    /** Lève IllegalArgumentException si le formulaire est incomplet ; vide si email/mot de passe faux. */
    public Optional<User> authenticate(String email, String password) {
        userValidation.verifierConnexion(email, password);
        return userRepository.findByEmail(email.trim())
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public User create(String username, String email, String password, String roleLibelle) {
        userValidation.verifierCreation(username, email, password);
        Role role = roleRepository.findByLibelle(roleLibelle)
                .orElseThrow(() -> new IllegalStateException("Rôle introuvable en base : " + roleLibelle));

        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setIdRole(role.getId());
        return userRepository.save(user);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public String roleLibelle(User user) {
        if (user.getIdRole() == null) {
            return null;
        }
        return roleRepository.findById(user.getIdRole()).map(Role::getLibelle).orElse(null);
    }
}
