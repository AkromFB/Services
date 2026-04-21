package com.example.service.controllers.API;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.models.Customer;
import com.example.service.services.CustomerService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/admin/api")
public class AdminRestApi {
    @Autowired
    CustomerService customerService;
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            
            List<Customer> customers = customerService.findAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
        }

    }
    
    
}
