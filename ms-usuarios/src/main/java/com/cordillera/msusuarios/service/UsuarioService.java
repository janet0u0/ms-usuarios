package com.cordillera.msusuarios.service;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.exception.ResourceNotFoundException;
import com.cordillera.msusuarios.model.Usuario;
import com.cordillera.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

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

    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Listando todos los usuarios");
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado con ID: " + id));
        return mapToDTO(usuario);
    }

    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        log.info("Creando nuevo usuario con email: {}", dto.getEmail());
        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                "El email ya está registrado: " + dto.getEmail());
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());
        return mapToDTO(repository.save(usuario));
    }

    public UsuarioResponseDTO login(String email, String password) {
        log.info("Intento de login para: {}", email);
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Credenciales inválidas"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        return mapToDTO(usuario);
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario actual = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Usuario no encontrado"));
        actual.setNombre(dto.getNombre());
        actual.setEmail(dto.getEmail());
        actual.setPassword(passwordEncoder.encode(dto.getPassword()));
        actual.setRol(dto.getRol());
        return mapToDTO(repository.save(actual));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        repository.deleteById(id);
    }
}