package com.cordillera.msusuarios.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Debe guardar correctamente el mensaje de error")
    void constructor_DeberiaGuardarMensaje() {

        ResourceNotFoundException exception =
                new ResourceNotFoundException("Usuario no encontrado");

        assertEquals(
                "Usuario no encontrado",
                exception.getMessage()
        );
    }
}