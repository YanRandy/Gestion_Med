package com.hopital.controller;

import com.hopital.service.SituationMatrimonialeService;
import org.springframework.context.ApplicationContext;
import randy.framework.annotation.RAuthenticated;
import randy.framework.annotation.RController;
import randy.framework.annotation.RRestApi;
import randy.framework.annotation.RUrlMapping;
import randy.framework.model.RApiResponse;

@RController
@RAuthenticated
public class SituationMatrimonialeController {

    @RRestApi
    @RUrlMapping(value = "/api/situations-matrimoniales", method = "GET")
    public RApiResponse list(ApplicationContext ctx) {
        return RApiResponse.ok("situations", ctx.getBean(SituationMatrimonialeService.class).findAll());
    }
}
