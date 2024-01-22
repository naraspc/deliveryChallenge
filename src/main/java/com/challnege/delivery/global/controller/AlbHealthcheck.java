package com.challnege.delivery.global.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbHealthcheck {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }

    @GetMapping("/health-check")
    public String healthcheckForDeploy() {
        return "up";
    }
}
