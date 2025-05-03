// src/main/java/com/amn/config/AdminInitializer.java
package com.amn.config;

import com.amn.entity.Admin;
import com.amn.entity.enums.Role;
import com.amn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@amn.com").isEmpty()) {
            Admin admin = Admin.builder()
                    .fullName("Super Admin")
                    .email("admin@amn.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("0600000000") // ✅ Add a valid phone number
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin created: admin@amn.com / admin123");
        }
    }
}
