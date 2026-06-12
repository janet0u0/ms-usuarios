package com.cordillera.msusuarios.dto;

import com.cordillera.msusuarios.model.Rol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioResponseDTOTest {

    @Test
    @DisplayName("Debe crear UsuarioResponseDTO correctamente")
    void usuarioResponseDTO_DeberiaCrearCorrectamente() {

        LocalDateTime creado = LocalDateTime.now();
        LocalDateTime actualizado = LocalDateTime.now();

        UsuarioResponseDTO dto =
                new UsuarioResponseDTO(
                        1L,
                        "Janet",
                        "janet@test.com",
                        Rol.ADMIN_SISTEMA,
                        creado,
                        actualizado
                );

        assertEquals(1L, dto.getId());
        assertEquals("Janet", dto.getNombre());
        assertEquals("janet@test.com", dto.getEmail());
        assertEquals(Rol.ADMIN_SISTEMA, dto.getRol());
        assertEquals(creado, dto.getCreadoEn());
        assertEquals(actualizado, dto.getActualizadoEn());
    }
}