package com.cordillera.msusuarios.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void passwordEncoder_deberiaFuncionar() {

        String raw = "123456";
        String encoded = passwordEncoder.encode(raw);

        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches(raw, encoded));
    }

    @Test
    void securityFilterChain_deberiaCargarse() {
        assertNotNull(securityFilterChain);
    }
}