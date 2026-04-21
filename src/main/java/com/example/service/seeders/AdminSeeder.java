package com.example.service.seeders;

import java.util.Locale;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.service.models.Admin;
import com.example.service.models.Customer;
import com.example.service.repository.AdminRepository;
import com.example.service.repository.CustomerRepository;
import com.example.service.services.AdminService;
import com.example.service.services.CustomerService;
import com.github.javafaker.Faker;

@Configuration
public class AdminSeeder {

    @SuppressWarnings("deprecation")
    @Bean
    public Faker faker() {
        // Seed fisso per risultati deterministici (opzionale)
        return new Faker(new Locale("it-IT"),new Random(12345));
    }


    @Bean
    CommandLineRunner seedAdmin(AdminService adminService, AdminRepository adminRepository) {
        return args -> {
            if (adminRepository.findByEmail("admin@example.com").isEmpty()) {
                Admin admin = adminService.createAdmin("admin@example.com", "admin123");
                System.out.println("ADMIN TOKEN: " + admin.getApiToken());
            }
        };
    }
    @Bean
    CommandLineRunner seedClienti(CustomerService customerService, CustomerRepository customerRepository) {
        return args ->{
            Faker faker = faker();
            System.out.println("Inizio generazione 20 clienti...");
            for (int i = 0; i < 20; i++) {
                // Genera i dati
                Customer customer = new Customer();
                customer.setName(faker.name().firstName()); // Genera Nome
            customer.setSurname(faker.name().lastName());
            customer.setEmail(faker.internet().emailAddress());
            customer.setCompany(faker.company().name());
            // Genera una password semplice (puoi usarla per hash in produzione)
            customer.setPassword(faker.internet().password());
            
            // Salva nel DB
            customerRepository.save(customer);
            }
        System.out.println("Completato 20 clienti");
        };
    }

}
