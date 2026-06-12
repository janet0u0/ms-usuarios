package com.cordillera.msusuarios.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    @DisplayName("Debe retornar 404 cuando recurso no existe")
    void handleNotFound_DeberiaRetornar404() {

        ResponseEntity<Map<String, String>> response =
                handler.handleNotFound(
                        new ResourceNotFoundException("Usuario no encontrado")
                );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Map<String, String> body = response.getBody();

        assertNotNull(body);
        assertEquals("Usuario no encontrado", body.get("error"));
    }

    @Test
    @DisplayName("Debe retornar 400 cuando ocurre IllegalArgumentException")
    void handleBadRequest_DeberiaRetornar400() {

        ResponseEntity<Map<String, String>> response =
                handler.handleBadRequest(
                        new IllegalArgumentException("Email duplicado")
                );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();

        assertNotNull(body);
        assertEquals("Email duplicado", body.get("error"));
    }

    @Test
    @DisplayName("Debe retornar 500 cuando ocurre excepción general")
    void handleGeneral_DeberiaRetornar500() {

        ResponseEntity<Map<String, String>> response =
                handler.handleGeneral(
                        new RuntimeException("Error inesperado")
                );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        Map<String, String> body = response.getBody();

        assertNotNull(body);

        assertEquals(
                "Error interno del servidor",
                body.get("error")
        );

        assertEquals(
                "Error inesperado",
                body.get("detalle")
        );
    }
}