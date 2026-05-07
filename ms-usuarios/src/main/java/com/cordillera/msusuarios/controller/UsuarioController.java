package com.cordillera.msusuarios.controller;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestiona usuarios y control de acceso (RBAC).
 * Patrón aplicado: DTO Pattern + Repository Pattern (a través del Service)
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor // ✅ reemplaza constructor manual
public class UsuarioController {

    private final UsuarioService service;

    /**
     * POST /api/usuarios/login
     */
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuario = service.login(
            dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(usuario);
    }

    /**
     * POST /api/usuarios/registrar
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}