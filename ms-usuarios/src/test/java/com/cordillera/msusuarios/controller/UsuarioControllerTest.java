package com.cordillera.msusuarios.controller;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.exception.ResourceNotFoundException;
import com.cordillera.msusuarios.model.Rol;
import com.cordillera.msusuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioResponseDTO responseDTO;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        responseDTO = new UsuarioResponseDTO(
                1L, "Janet", "janet@gmail.com",
                Rol.ADMIN_SISTEMA,
                LocalDateTime.now(), LocalDateTime.now()
        );
        requestDTO = new UsuarioRequestDTO(
                "Janet", "janet@gmail.com", "123456", Rol.ADMIN_SISTEMA
        );
    }

    // ── GET /api/usuarios ─────────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista de usuarios cuando existen registros")
    void listarTodos_CuandoExistenUsuarios_DeberiaRetornarLista()
            throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Janet"))
                .andExpect(jsonPath("$[0].email").value("janet@gmail.com"))
                .andExpect(jsonPath("$[0].rol").value("ADMIN_SISTEMA"));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay usuarios")
    void listarTodos_CuandoNoHayUsuarios_DeberiaRetornarVacio()
            throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── GET /api/usuarios/{id} ────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar usuario cuando existe el ID buscado")
    void buscarPorId_CuandoUsuarioExiste_DeberiaRetornarUsuario()
            throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Janet"))
                .andExpect(jsonPath("$.email").value("janet@gmail.com"));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el usuario no existe")
    void buscarPorId_CuandoNoExiste_DeberiaRetornar404()
            throws Exception {
        when(usuarioService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException(
                        "Usuario no encontrado con ID: 99"));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Usuario no encontrado con ID: 99"));
    }

    // ── POST /api/usuarios/login ──────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar 200 con credenciales correctas")
    void login_CuandoCredencialesCorrectas_DeberiaRetornar200()
            throws Exception {
        when(usuarioService.login("janet@gmail.com", "123456"))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("janet@gmail.com"))
                .andExpect(jsonPath("$.rol").value("ADMIN_SISTEMA"));
    }

    @Test
    @DisplayName("Debe retornar 404 con credenciales incorrectas")
    void login_CuandoCredencialesIncorrectas_DeberiaRetornar404()
            throws Exception {
        when(usuarioService.login(anyString(), anyString()))
                .thenThrow(new ResourceNotFoundException("Credenciales inválidas"));

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"));
    }

    @Test
    @DisplayName("Debe retornar 400 con password incorrecto")
    void login_CuandoPasswordIncorrecto_DeberiaRetornar400()
            throws Exception {
        when(usuarioService.login(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Credenciales inválidas"));

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"));
    }

    // ── POST /api/usuarios/registrar ──────────────────────────────────────

    @Test
    @DisplayName("Debe registrar usuario y retornar 201")
    void registrar_CuandoDatosValidos_DeberiaRetornar201()
            throws Exception {
        when(usuarioService.guardar(any(UsuarioRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("janet@gmail.com"))
                .andExpect(jsonPath("$.rol").value("ADMIN_SISTEMA"));
    }

    @Test
    @DisplayName("Debe retornar 400 si email ya está registrado")
    void registrar_CuandoEmailDuplicado_DeberiaRetornar400()
            throws Exception {
        when(usuarioService.guardar(any(UsuarioRequestDTO.class)))
                .thenThrow(new IllegalArgumentException(
                        "El email ya está registrado: janet@gmail.com"));

        mockMvc.perform(post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("El email ya está registrado: janet@gmail.com"));
    }

    @Test
    @DisplayName("Debe retornar 400 si email tiene formato inválido")
    void registrar_CuandoEmailInvalido_DeberiaRetornar400()
            throws Exception {
        UsuarioRequestDTO invalido = new UsuarioRequestDTO(
                "Janet", "emailinvalido", "123456", Rol.ADMIN_SISTEMA
        );

        mockMvc.perform(post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe retornar 400 si nombre está vacío")
    void registrar_CuandoNombreVacio_DeberiaRetornar400()
            throws Exception {
        UsuarioRequestDTO invalido = new UsuarioRequestDTO(
                "", "janet@gmail.com", "123456", Rol.ADMIN_SISTEMA
        );

        mockMvc.perform(post("/api/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ── PUT /api/usuarios/{id} ────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar usuario y retornar 200")
    void actualizar_CuandoUsuarioExiste_DeberiaRetornar200()
            throws Exception {
        when(usuarioService.actualizar(eq(1L), any(UsuarioRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Janet"));
    }

    @Test
    @DisplayName("Debe retornar 404 si usuario a actualizar no existe")
    void actualizar_CuandoNoExiste_DeberiaRetornar404()
            throws Exception {
        when(usuarioService.actualizar(eq(99L), any(UsuarioRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException(
                        "Usuario no encontrado con ID: 99"));

        mockMvc.perform(put("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Usuario no encontrado con ID: 99"));
    }

    // ── DELETE /api/usuarios/{id} ─────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar usuario y retornar 204")
    void eliminar_CuandoUsuarioExiste_DeberiaRetornar204()
            throws Exception {
        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Debe retornar 404 si usuario a eliminar no existe")
    void eliminar_CuandoNoExiste_DeberiaRetornar404()
            throws Exception {
        doThrow(new ResourceNotFoundException(
                "Usuario no encontrado con ID: 99"))
                .when(usuarioService).eliminar(99L);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Usuario no encontrado con ID: 99"));
    }
    @Test
    @DisplayName("Debe retornar 500 ante error interno no controlado")
    void error_CuandoExcepcionGenerica_DeberiaRetornar500()
        throws Exception {
        when(usuarioService.listarTodos())
             .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/api/usuarios"))
             .andExpect(status().isInternalServerError())
             .andExpect(jsonPath("$.error")
                   .value("Error interno del servidor"));
    }
}