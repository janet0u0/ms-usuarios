package com.cordillera.msusuarios.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestDTOTest {

    @Test
    @DisplayName("Debe almacenar email y password correctamente")
    void loginRequestDTO_DeberiaGuardarDatos() {

        LoginRequestDTO dto = new LoginRequestDTO();

        dto.setEmail("janet@test.com");
        dto.setPassword("123456");

        assertEquals("janet@test.com", dto.getEmail());
        assertEquals("123456", dto.getPassword());
    }
}