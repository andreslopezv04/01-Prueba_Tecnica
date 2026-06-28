package com.vortexbird.orders.infrastructure.config;

import com.vortexbird.orders.domain.model.Role;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserJpaRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@orders.com").isEmpty()) {
                UserJpaEntity admin = new UserJpaEntity();
                admin.setFirstName("Andres");
                admin.setLastName("Admin");
                admin.setEmail("admin@orders.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRoles(Set.of(Role.ADMIN));
                repo.save(admin);
            }
            if (repo.findByEmail("operator@orders.com").isEmpty()) {
                UserJpaEntity operator = new UserJpaEntity();
                operator.setFirstName("Messi");
                operator.setLastName("Operator");
                operator.setEmail("operator@orders.com");
                operator.setPassword(encoder.encode("operator123"));
                operator.setRoles(Set.of(Role.OPERATOR));
                repo.save(operator);
            }
            if (repo.findByEmail("operator2@orders.com").isEmpty()) {
                UserJpaEntity operator2 = new UserJpaEntity();
                operator2.setFirstName("Diego");
                operator2.setLastName("Operator");
                operator2.setEmail("operator2@orders.com");
                operator2.setPassword(encoder.encode("operator123"));
                operator2.setRoles(Set.of(Role.OPERATOR));
                repo.save(operator2);
            }
        };
    }
}