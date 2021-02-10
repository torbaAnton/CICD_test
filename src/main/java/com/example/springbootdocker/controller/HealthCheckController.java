package com.example.springbootdocker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Operation(summary = "Simple health check endpoint")
    @GetMapping("/check")
    public String healthCheckEndpoint (){
        return "OK";
    }
}
