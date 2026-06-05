package com.cyberaudit.config;

import com.cyberaudit.model.User;
import com.cyberaudit.model.enums.Role;
import com.cyberaudit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username}") String adminUsername,
            @Value("${app.admin.password}") String adminPassword,
            @Value("${app.admin.email}") String adminEmail) {
        return args -> {
            userRepository.findByUsername(adminUsername).ifPresentOrElse(
                    user -> {
                        user.setPassword(passwordEncoder.encode(adminPassword));
                        user.setEnabled(true);
                        user.setRole(Role.SUPER_ADMIN);
                        userRepository.save(user);
                    },
                    () -> {
                        User admin = new User();
                        admin.setUsername(adminUsername);
                        admin.setPassword(passwordEncoder.encode(adminPassword));
                        admin.setEmail(adminEmail);
                        admin.setFullName("System Administrator");
                        admin.setRole(Role.SUPER_ADMIN);
                        admin.setEnabled(true);
                        userRepository.save(admin);
                    });
        };
    }
}
