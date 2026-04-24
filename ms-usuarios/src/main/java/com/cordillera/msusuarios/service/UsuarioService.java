package com.cordillera.msusuarios.service;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.exception.ResourceNotFoundException;
import com.cordillera.msusuarios.model.Usuario;
import com.cordillera.msusuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // MAPPER
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

    // LISTAR
    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // BUSCAR
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return mapToDTO(usuario);
    }

    // GUARDAR
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {

        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());

        return mapToDTO(repository.save(usuario));
    }

    // ACTUALIZAR
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {

        Usuario actual = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        actual.setNombre(dto.getNombre());
        actual.setEmail(dto.getEmail());
        actual.setPassword(dto.getPassword());
        actual.setRol(dto.getRol());

        return mapToDTO(repository.save(actual));
    }

    // ELIMINAR
    public void eliminar(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        repository.deleteById(id);
    }
}