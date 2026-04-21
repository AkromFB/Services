package com.example.service.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class AuthController {
    @GetMapping("/login")
    public String getLoginPage(Authentication authentication) {
        if(authentication != null){
            if(authentication.isAuthenticated()){
                return "redirect:/";
                
            }
        }
        return "login"; 
    }
    @GetMapping("/register")
    public String getRegisterPage(Authentication authentication) {
        if(authentication != null){
            if(authentication.isAuthenticated()){
                return "redirect:/";
            }
        }
        return "register"; 
    }

    
    
}
