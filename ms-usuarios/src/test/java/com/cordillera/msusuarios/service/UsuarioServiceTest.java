package com.cordillera.msusuarios.service;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.exception.ResourceNotFoundException;
import com.cordillera.msusuarios.model.Rol;
import com.cordillera.msusuarios.model.Usuario;
import com.cordillera.msusuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Janet");
        usuario.setEmail("janet@test.com");
        usuario.setPassword("123_encriptado");
        usuario.setRol(Rol.ADMIN_SISTEMA);
        usuario.setCreadoEn(LocalDateTime.now());
        usuario.setActualizadoEn(LocalDateTime.now());

        requestDTO = new UsuarioRequestDTO(
                "Janet", "janet@test.com", "123", Rol.ADMIN_SISTEMA
        );
    }

    // ── listarTodos() ────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe listar todos los usuarios")
    void listarTodos_CuandoExistenUsuarios_DeberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Janet", resultado.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay usuarios")
    void listarTodos_CuandoNoHayUsuarios_DeberiaRetornarVacio() {
        when(repository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDTO> resultado = service.listarTodos();

        assertEquals(0, resultado.size());
        verify(repository).findAll();
    }

    // ── buscarPorId() ────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe buscar usuario por ID")
    void buscarPorId_CuandoUsuarioExiste_DeberiaRetornarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Janet", resultado.getNombre());
        assertEquals(Rol.ADMIN_SISTEMA, resultado.getRol());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe")
    void buscarPorId_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.buscarPorId(1L));
        verify(repository).findById(1L);
    }

    // ── guardar() ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar usuario correctamente")
    void guardar_CuandoDatosSonValidos_DeberiaGuardarUsuario() {
        when(repository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("123_encriptado");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals("Janet", resultado.getNombre());
        verify(passwordEncoder).encode("123");
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando email ya existe")
    void guardar_CuandoEmailExiste_DeberiaLanzarExcepcion() {
        when(repository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.guardar(requestDTO));
        verify(repository, never()).save(any());
    }

    // ── login() ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar usuario con credenciales correctas")
    void login_CuandoCredencialesCorrectas_DeberiaRetornarUsuario() {
        when(repository.findByEmail("janet@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123", "123_encriptado"))
                .thenReturn(true);

        UsuarioResponseDTO resultado = service.login("janet@test.com", "123");

        assertNotNull(resultado);
        assertEquals("janet@test.com", resultado.getEmail());
        assertEquals(Rol.ADMIN_SISTEMA, resultado.getRol());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando email no existe en login")
    void login_CuandoEmailNoExiste_DeberiaLanzarExcepcion() {
        when(repository.findByEmail("noexiste@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.login("noexiste@test.com", "123"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando password es incorrecto")
    void login_CuandoPasswordIncorrecto_DeberiaLanzarExcepcion() {
        when(repository.findByEmail("janet@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongpass", "123_encriptado"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.login("janet@test.com", "wrongpass"));
    }

    // ── actualizar() ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar usuario correctamente")
    void actualizar_CuandoUsuarioExiste_DeberiaActualizar() {
        UsuarioRequestDTO dtoNuevo = new UsuarioRequestDTO(
                "Janet Actualizada", "janet@test.com", "newpass", Rol.ANALISTA
        );
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newpass")).thenReturn("newpass_enc");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.actualizar(1L, dtoNuevo);

        assertNotNull(resultado);
        verify(repository).save(any(Usuario.class));
        verify(passwordEncoder).encode("newpass");
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar usuario inexistente")
    void actualizar_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.actualizar(99L, requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si nuevo email ya pertenece a otro usuario")
    void actualizar_CuandoEmailDuplicado_DeberiaLanzarExcepcion() {
        UsuarioRequestDTO dtoNuevo = new UsuarioRequestDTO(
                "Janet", "otro@test.com", "123", Rol.ADMIN_SISTEMA
        );
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("otro@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizar(1L, dtoNuevo));
        verify(repository, never()).save(any());
    }

    // ── eliminar() ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar usuario correctamente")
    void eliminar_CuandoUsuarioExiste_DeberiaEliminar() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar usuario inexistente")
    void eliminar_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.eliminar(1L));
        verify(repository, never()).deleteById(any());
    }
    @Test
        @DisplayName("Debe retornar descripcion del rol correctamente")
        void rol_getDescripcion_DeberiaRetornarDescripcion() {
        assertEquals("Administrador del Sistema", Rol.ADMIN_SISTEMA.getDescripcion());
        assertEquals("Alta Gerencia", Rol.EJECUTIVO.getDescripcion());
        assertEquals("Analista de Negocio", Rol.ANALISTA.getDescripcion());
        assertEquals("Administrador de Datos", Rol.SUPERVISOR.getDescripcion());
}
}