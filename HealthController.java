package com.cmdb.integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class HealthController {

    @GetMapping(value = "/health")
    @Operation(summary = "Service health check")
    public Health healthCheck(){
        return Health.up().build();
    }
}

