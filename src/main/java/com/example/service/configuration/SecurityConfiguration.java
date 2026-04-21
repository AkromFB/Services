package com.example.service.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.service.models.Admin;
import com.example.service.repository.AdminRepository;
import com.example.service.services.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AdminApiTokenFilter adminApiTokenFilter;
    @Autowired
    private AdminRepository adminRepository;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
       return http
       .cors(Customizer.withDefaults()) 
       .csrf(csrf -> csrf.disable())
       .authorizeHttpRequests(req ->{
           req
           .requestMatchers("/", "/css/**", "/js/**", "/plans","/register","/login","/auth/**").permitAll()
           .requestMatchers("/profile").hasRole("CUSTOMER")
           .anyRequest().authenticated();
        })
        .formLogin(form->form
            .loginPage("/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .permitAll())
        .logout(logout -> logout.permitAll())
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                // String path = request.getRequestURI();
                // if (path.startsWith("/admin")) {
                //     response.sendRedirect("/admin/login");
                // } else {
                //     response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                // }
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            })
        )
        .build();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/admin/**", "/auth/admin/login")
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(req -> req
                .requestMatchers("/auth/admin/login").permitAll()
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/auth/admin/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    String email = authentication.getName();
                    Admin admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Admin non trovato"));

                    request.getSession().setAttribute("ADMIN_API_TOKEN", admin.getApiToken());
                    response.sendRedirect("/admin");
                })
                .permitAll()
            )
            .logout(logout -> logout
            .logoutUrl("/admin/logout")
            .logoutSuccessUrl("/admin/login?logout")
            .permitAll()
        )
        .addFilterBefore(adminApiTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

}
