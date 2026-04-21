package com.example.service.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.service.models.Admin;
import com.example.service.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public Admin createAdmin(String email, String rawPassword) {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setApiToken(generateApiToken());
        return adminRepository.save(admin);
    }
    public Admin loginAttemptAdmin(Admin admin) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(admin.getEmail());
        Admin AdminDB = getAdminByEmail(admin.getEmail());
        if (!checkPassword(admin.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Credenziali errate");
        }
        return AdminDB;
    }
      public Admin getAdminByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("username inesistente"));
        admin.setId(-1l);
        admin.setPassword("");
        return admin;
    }
    private boolean checkPassword(String rawPassword, String encodedPasswordFromDb) {
        return passwordEncoder.matches(rawPassword, encodedPasswordFromDb);
    }

    private String generateApiToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}