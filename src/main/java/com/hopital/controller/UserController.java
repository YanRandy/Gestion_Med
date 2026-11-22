package com.hopital.controller;

import com.hopital.entity.User;
import com.hopital.service.PatientService;
import com.hopital.service.UserService;
import org.springframework.context.ApplicationContext;
import randy.framework.annotation.RAuthenticated;
import randy.framework.annotation.RController;
import randy.framework.annotation.RRestApi;
import randy.framework.annotation.RUrlMapping;
import randy.framework.model.RApiResponse;
import randy.framework.model.RSession;

import java.util.Optional;

@RController
public class UserController {

    private static final String HOME_PATIENT = "/mon-dossier";
    private static final String HOME_PERSONNEL = "/patients";

    @RUrlMapping(value = "/", method = "GET")
    public String landing() {
        return "landing";
    }

    @RUrlMapping(value = "/login", method = "GET")
    public String loginPage() {
        return "login";
    }

    @RAuthenticated(roles = {UserService.ROLE_PATIENT})
    @RUrlMapping(value = "/mon-dossier", method = "GET")
    public String monDossier() {
        return "mon-dossier";
    }

    @RRestApi
    @RUrlMapping(value = "/api/login", method = "POST")
    public RApiResponse login(String email, String password, RSession session, ApplicationContext ctx) {
        UserService userService = ctx.getBean(UserService.class);
        Optional<User> user;
        try {
            user = userService.authenticate(email, password);
        } catch (IllegalArgumentException e) {
            return RApiResponse.error(e.getMessage());
        }
        if (user.isEmpty()) {
            return RApiResponse.error(401, "Email ou mot de passe incorrect.");
        }
        String role = userService.roleLibelle(user.get());
        session.set("user", user.get());
        session.set("role", role);

        return RApiResponse.ok("role", role)
                .with("redirect", UserService.ROLE_PATIENT.equals(role) ? HOME_PATIENT : HOME_PERSONNEL);
    }

    @RRestApi
    @RAuthenticated
    @RUrlMapping(value = "/api/logout", method = "POST")
    public RApiResponse logout(RSession session) {
        session.invalidate();
        return RApiResponse.ok();
    }

    @RRestApi
    @RAuthenticated
    @RUrlMapping(value = "/api/me", method = "GET")
    public RApiResponse me(RSession session, ApplicationContext ctx) {
        User user = session.get("user", User.class);
        String role = session.get("role", String.class);

        RApiResponse response = RApiResponse.ok("user", user).with("role", role);
        if (UserService.ROLE_PATIENT.equals(role)) {
            response.with("patient", ctx.getBean(PatientService.class).findByUser(user.getId()).orElse(null));
        }
        return response;
    }
}
