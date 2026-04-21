package com.example.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.service.models.Admin;
import com.example.service.models.Customer;
import com.example.service.repository.AdminRepository;
import com.example.service.repository.CustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdminRepository adminRepository;
    
    @Value("${spring.security.user.name}")
    private String adminUsername;
    
    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return User
                    .withUsername(admin.getEmail())
                    .password(admin.getPassword())
                    .roles("ADMIN")
                    .build();
        }
        var customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            return User
                    .withUsername(customer.getEmail())
                    .password(customer.getPassword())
                    .roles("CUSTOMER")
                    .build();
        }

        throw new UsernameNotFoundException("Credenziali errate");
    }
    
}
