package com.cordillera.msusuarios.service;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.exception.ResourceNotFoundException;
import com.cordillera.msusuarios.model.Rol;
import com.cordillera.msusuarios.model.Usuario;
import com.cordillera.msusuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
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

    // ── listarTodos ────────────────────────────────────────────────────

    @Test
    void listarTodos_CuandoExistenUsuarios_DeberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = service.listarTodos();

        assertEquals(1, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void listarTodos_CuandoNoHayUsuarios_DeberiaRetornarVacio() {
        when(repository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDTO> resultado = service.listarTodos();

        assertEquals(0, resultado.size());
        verify(repository).findAll();
    }

    // ── buscarPorId ────────────────────────────────────────────────────

    @Test
    void buscarPorId_CuandoExiste_DeberiaRetornarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.buscarPorId(1L));
    }

    // ── guardar ────────────────────────────────────────────────────────

    @Test
    void guardar_CuandoValido_DeberiaGuardar() {
        when(repository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("123_encriptado");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.guardar(requestDTO);

        assertNotNull(resultado);
        verify(repository).save(any());
    }

    @Test
    void guardar_CuandoEmailExiste_DeberiaLanzarExcepcion() {
        when(repository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.guardar(requestDTO));
    }

    // ── login ──────────────────────────────────────────────────────────

    @Test
    void login_Correcto_DeberiaRetornarUsuario() {
        when(repository.findByEmail("janet@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123", "123_encriptado"))
                .thenReturn(true);

        UsuarioResponseDTO resultado = service.login("janet@test.com", "123");

        assertNotNull(resultado);
    }

    @Test
    void login_EmailNoExiste_DeberiaLanzarExcepcion() {
        when(repository.findByEmail("no@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.login("no@test.com", "123"));
    }

    @Test
    void login_PasswordIncorrecto_DeberiaLanzarExcepcion() {
        when(repository.findByEmail("janet@test.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrong", "123_encriptado"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.login("janet@test.com", "wrong"));
    }

    // ── actualizar (COMPLETO 100% BRANCHES) ────────────────────────────

    @Test
    void actualizar_CuandoExiste_DeberiaActualizar() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Janet Updated", "janet@test.com", "newpass", Rol.ANALISTA
        );

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newpass")).thenReturn("newpass_enc");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.actualizar(1L, dto);

        assertNotNull(resultado);
        verify(repository).save(any());
    }

    @Test
    void actualizar_NoExiste_DeberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.actualizar(99L, requestDTO));
    }

    @Test
    void actualizar_EmailDuplicado_DeberiaLanzarExcepcion() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Janet", "otro@test.com", "123", Rol.ADMIN_SISTEMA
        );

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("otro@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizar(1L, dto));
    }

    // 🔥 NUEVO TEST QUE TE FALTABA PARA 100% BRANCH COVERAGE
    @Test
    void actualizar_EmailCambiaPeroNoExisteDuplicado_DeberiaActualizar() {

        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Janet Updated",
                "nuevo@email.com",
                "newpass",
                Rol.ANALISTA
        );

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("nuevo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("newpass")).thenReturn("newpass_enc");
        when(repository.save(any(Usuario.class)))
                .thenAnswer(i -> i.getArgument(0));

        UsuarioResponseDTO resultado = service.actualizar(1L, dto);

        assertNotNull(resultado);

        verify(repository).findById(1L);
        verify(repository).existsByEmail("nuevo@email.com");
        verify(passwordEncoder).encode("newpass");
        verify(repository).save(any());
    }

    // ── eliminar ───────────────────────────────────────────────────────

    @Test
    void eliminar_CuandoExiste_DeberiaEliminar() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> service.eliminar(1L));
    }

    // ── roles ──────────────────────────────────────────────────────────

    @Test
    void roles_DeberianRetornarDescripcion() {
        assertEquals("Administrador del Sistema", Rol.ADMIN_SISTEMA.getDescripcion());
        assertEquals("Alta Gerencia", Rol.EJECUTIVO.getDescripcion());
        assertEquals("Analista de Negocio", Rol.ANALISTA.getDescripcion());
        assertEquals("Administrador de Datos", Rol.SUPERVISOR.getDescripcion());
    }
}