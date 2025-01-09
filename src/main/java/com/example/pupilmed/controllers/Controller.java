package com.example.pupilmed.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller{

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    public String homeAlias() {
        return "home";
    }

}

