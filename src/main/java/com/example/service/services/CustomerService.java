package com.example.service.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.service.models.Customer;
import com.example.service.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

        
    private boolean checkPassword(String rawPassword, String encodedPasswordFromDb) {
        return passwordEncoder.matches(rawPassword, encodedPasswordFromDb);
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("username inesistente"));
        customer.setId(-1);
        customer.setPassword("");
        return customer;
    }

    public void registerCustomer(Customer customer) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        customer.setPassword(encoder.encode(customer.getPassword()));
        customerRepository.save(customer);
    }

    public Customer loginAttemptCustomer(Customer customer) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(customer.getEmail());
        Customer customerDB = getCustomerByEmail(customer.getEmail());
        if (!checkPassword(customer.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Credenziali errate");
        }
        return customerDB;
    }

    // -------------API Only Admin------------
    public List<Customer>findAllCustomers(){
        return customerRepository.findAll();
    }

}
