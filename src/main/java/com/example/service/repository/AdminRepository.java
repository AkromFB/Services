package com.example.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.service.models.Admin;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Long>{
    Optional<Admin> findByApiToken(String apiToken);
    Optional<Admin> findByEmail(String email);
}
