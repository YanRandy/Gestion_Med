package com.hopital.controller;

import com.hopital.entity.Patient;
import com.hopital.entity.User;
import com.hopital.service.PatientService;
import org.springframework.context.ApplicationContext;
import randy.framework.annotation.RAuthenticated;
import randy.framework.annotation.RController;
import randy.framework.annotation.RRestApi;
import randy.framework.annotation.RUrlMapping;
import randy.framework.model.RApiResponse;

@RController
@RAuthenticated(roles = {"MEDECIN"})
public class PatientController {

    @RUrlMapping(value = "/patients", method = "GET")
    public String page() {
        return "patients";
    }

    @RRestApi
    @RUrlMapping(value = "/api/patients", method = "GET")
    public RApiResponse list(String nom, ApplicationContext ctx) {
        return RApiResponse.ok("patients", ctx.getBean(PatientService.class).search(nom));
    }

    @RRestApi
    @RUrlMapping(value = "/api/patients/detail", method = "GET")
    public RApiResponse detail(Integer id, ApplicationContext ctx) {
        if (id == null) {
            return RApiResponse.error("Paramètre id manquant.");
        }
        return ctx.getBean(PatientService.class).findById(id)
                .map(p -> RApiResponse.ok("patient", p))
                .orElseGet(() -> RApiResponse.error(404, "Patient introuvable."));
    }

    @RRestApi
    @RUrlMapping(value = "/api/patients", method = "POST")
    public RApiResponse create(Patient patient, ApplicationContext ctx) {
        try {
            return RApiResponse.ok("patient", ctx.getBean(PatientService.class).create(patient));
        } catch (IllegalArgumentException e) {
            return RApiResponse.error(e.getMessage());
        }
    }

    @RRestApi
    @RUrlMapping(value = "/api/patients/modifier", method = "POST")
    public RApiResponse update(Patient patient, ApplicationContext ctx) {
        try {
            return RApiResponse.ok("patient", ctx.getBean(PatientService.class).update(patient));
        } catch (IllegalArgumentException e) {
            return RApiResponse.error(e.getMessage());
        }
    }

    @RRestApi
    @RUrlMapping(value = "/api/patients/supprimer", method = "POST")
    public RApiResponse delete(Integer id, ApplicationContext ctx) {
        try {
            ctx.getBean(PatientService.class).delete(id);
            return RApiResponse.ok();
        } catch (IllegalArgumentException e) {
            return RApiResponse.error(e.getMessage());
        }
    }

    @RRestApi
    @RUrlMapping(value = "/api/patients/acces", method = "POST")
    public RApiResponse createAccess(Integer idPatient, String email, String password, ApplicationContext ctx) {
        if (idPatient == null) {
            return RApiResponse.error("Paramètre idPatient manquant.");
        }
        try {
            User user = ctx.getBean(PatientService.class).createAccess(idPatient, email, password);
            return RApiResponse.ok("user", user);
        } catch (IllegalArgumentException e) {
            return RApiResponse.error(e.getMessage());
        }
    }
}
