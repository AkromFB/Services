package com.example.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.service.models.Customer;
import com.example.service.services.CustomerService;

@Controller
public class HomeController {
    
    @Autowired
    CustomerService customerService;

    @GetMapping("/")
    public String getHomePage(Authentication authentication, Model model) {

         if(authentication != null){
            if(authentication.isAuthenticated()){
                Customer customer = customerService.getCustomerByEmail(authentication.getName());
                model.addAttribute("customer",customer);
            }
        }

        return "home";
    }
   
    @GetMapping("/plans")
    public String getPlansPage(Authentication authentication,Model model) {
        if(authentication != null){
            if(authentication.isAuthenticated()){
                Customer customer = customerService.getCustomerByEmail(authentication.getName());
                model.addAttribute("customer",customer);
            }
        }
        return "plans";
    }
    @GetMapping("/profile")
    public String getProfilePage(Authentication authentication,Model model) {
         if(authentication != null){
            if(authentication.isAuthenticated()){
                Customer customer = customerService.getCustomerByEmail(authentication.getName());
                model.addAttribute("customer",customer);
            }
        }
        return "profile";
    }
    
   
    
}
