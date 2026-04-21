package com.example.service.models;

import java.time.LocalDate;
// import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = true, length = 15)
    private String name;
    @Column(nullable = true, length = 15)
    private String surname;
    @Column(nullable = true, unique = true, length = 50)
    private String email;
    @Column(nullable = false)
    private String company;
    @Column(nullable = false)
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    public Customer(Map<String, Object> params) {
        if (params != null) {
            name = (String) params.get("name");
            surname = (String) params.get("surname");
            email = (String) params.get("email");
            company = (String) params.get("company");
            password = (String) params.get("password");
        }
    }



}
