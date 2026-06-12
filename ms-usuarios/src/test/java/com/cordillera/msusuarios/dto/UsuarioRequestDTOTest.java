package com.cordillera.msusuarios.dto;

import com.cordillera.msusuarios.model.Rol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioRequestDTOTest {

    @Test
    @DisplayName("Debe crear UsuarioRequestDTO correctamente")
    void usuarioRequestDTO_DeberiaCrearCorrectamente() {

        UsuarioRequestDTO dto =
                new UsuarioRequestDTO(
                        "Janet",
                        "janet@test.com",
                        "123456",
                        Rol.ADMIN_SISTEMA
                );

        assertEquals("Janet", dto.getNombre());
        assertEquals("janet@test.com", dto.getEmail());
        assertEquals("123456", dto.getPassword());
        assertEquals(Rol.ADMIN_SISTEMA, dto.getRol());
    }
}