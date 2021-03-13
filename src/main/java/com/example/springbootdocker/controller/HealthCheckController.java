package com.example.springbootdocker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckController {

    @Value("${endpoint-message}")
    private String message;

    @Operation(summary = "Simple health check endpoint")
    @GetMapping("/check")
    public String healthCheckEndpoint (){
        return "OK!";
    }

    @Operation(summary = "Property message check endpoint")
    @GetMapping("/message")
    public String propertyMessageEndpoint (){
        return String.format("Message is: %s", message);
    }

}
