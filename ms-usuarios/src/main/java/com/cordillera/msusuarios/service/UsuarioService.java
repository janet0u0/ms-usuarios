package com.cordillera.msusuarios.service;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.exception.ResourceNotFoundException;
import com.cordillera.msusuarios.model.Usuario;
import com.cordillera.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contiene la lógica de negocio del microservicio.
 *
 * Patrón aplicado: Repository Pattern
 * Accede a datos exclusivamente a través de UsuarioRepository,
 * sin conocer los detalles de la base de datos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    /**
     * Convierte entidad Usuario a UsuarioResponseDTO.
     * Separa la capa de persistencia de la capa de presentación.
     */
    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getRol(),
                u.getCreadoEn(),
                u.getActualizadoEn()
        );
    }

    /**
     * Lista todos los usuarios del sistema.
     */
    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Listando todos los usuarios");
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * Busca un usuario por ID.
     * Lanza ResourceNotFoundException si no existe.
     */
    public UsuarioResponseDTO buscarPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + id));
        return mapToDTO(usuario);
    }

    /**
     * Crea un nuevo usuario.
     * Valida que el email no esté duplicado.
     */
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        log.info("Creando nuevo usuario con email: {}", dto.getEmail());

        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "El email ya está registrado: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        return mapToDTO(repository.save(usuario));
    }

    /**
     * Actualiza un usuario existente.
     * Lanza ResourceNotFoundException si no existe.
     */
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando usuario con ID: {}", id);

        Usuario actual = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + id));

        actual.setNombre(dto.getNombre());
        actual.setEmail(dto.getEmail());
        actual.setPassword(dto.getPassword());
        actual.setRol(dto.getRol());

        return mapToDTO(repository.save(actual));
    }

    /**
     * Elimina un usuario por ID.
     * Lanza ResourceNotFoundException si no existe.
     */
    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Usuario no encontrado con ID: " + id);
        }

        repository.deleteById(id);
    }
}