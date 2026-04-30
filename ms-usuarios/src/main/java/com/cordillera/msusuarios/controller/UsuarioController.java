package com.cordillera.msusuarios.controller;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST - Microservicio de Usuarios
 * Grupo Cordillera
 *
 * Gestiona autenticación y control de acceso (RBAC).
 * Recibe DTOs para separar la capa de presentación del modelo.
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    // Inyección por constructor - mejor práctica que @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    /**
     * GET /api/usuarios
     * Lista todos los usuarios del sistema
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * GET /api/usuarios/{id}
     * Busca un usuario por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * POST /api/usuarios
     * Crea un nuevo usuario
     * @Valid activa las validaciones del DTO
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> guardar(
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(dto));
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario existente
     * @Valid activa las validaciones del DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina un usuario por su ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}