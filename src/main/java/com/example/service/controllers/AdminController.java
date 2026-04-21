package com.example.service.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("")
    public String getAdminHome() {
        return "admin";
    }
    @GetMapping("/login")
    public String getAdminLogin() {
        return "admin-login";
    }
    @GetMapping("/session-token")
    @ResponseBody
    public Map<String, String> getAdminSessionToken(HttpSession session) {
        String token = (String) session.getAttribute("ADMIN_API_TOKEN");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non disponibile");
        }
        return Map.of("token", token);
    }
    
}
