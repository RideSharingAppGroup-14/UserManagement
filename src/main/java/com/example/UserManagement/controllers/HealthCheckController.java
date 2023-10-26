package com.example.UserManagement.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public String checkHealth() {
        // You can add custom logic to check the health of your application here.
        // For simplicity, we'll just return a status message.
        return "Application is up and running!";
    }
}
