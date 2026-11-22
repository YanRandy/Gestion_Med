package com.hopital.controller;

import com.hopital.service.NationaliteService;
import org.springframework.context.ApplicationContext;
import randy.framework.annotation.RAuthenticated;
import randy.framework.annotation.RController;
import randy.framework.annotation.RRestApi;
import randy.framework.annotation.RUrlMapping;
import randy.framework.model.RApiResponse;

@RController
@RAuthenticated
public class NationaliteController {

    @RRestApi
    @RUrlMapping(value = "/api/nationalites", method = "GET")
    public RApiResponse list(ApplicationContext ctx) {
        return RApiResponse.ok("nationalites", ctx.getBean(NationaliteService.class).findAll());
    }
}
