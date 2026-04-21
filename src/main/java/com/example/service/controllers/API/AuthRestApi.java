package com.example.service.controllers.API;

import com.example.service.services.CustomUserDetailsService;
import com.example.service.services.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestController;
import com.example.service.models.Customer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/auth")
public class AuthRestApi {
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository =
        new HttpSessionSecurityContextRepository();
    // private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public ResponseEntity<?> postRegister(@RequestBody  Map<String, Object> params) {
        try {
            Customer customer = new Customer(params);
            customerService.registerCustomer(customer);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("error","Registrazione avvenuta con successo"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
        }
    }

    @PostMapping("/login")
    public  ResponseEntity<?> postLogin(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpServletResponse response) {
        try {
    
             UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.unauthenticated(params.get("email"), params.get("password"));

            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, response);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Login effetuato con successo"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
        }
    }
    
    @PostMapping("/admin/login")
    public  ResponseEntity<?> postAdminLogin(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpServletResponse response) {
        try {
    
             UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.unauthenticated(params.get("email"), params.get("password"));

            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, response);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Login effetuato con successo"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",e.getMessage()));
        }
    }
    

}
