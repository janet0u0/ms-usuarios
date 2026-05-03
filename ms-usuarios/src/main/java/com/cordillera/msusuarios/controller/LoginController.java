package com.cordillera.msusuarios.controller;

import com.cordillera.msusuarios.model.Usuario;
import com.cordillera.msusuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Controller de Autenticación - MS-Usuarios
 * Valida credenciales y retorna el rol del usuario
 * para que el BFF genere el dashboard correspondiente
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    /**
     * POST /api/auth/login
     * Recibe email y password
     * Retorna nombre, email y rol del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> credenciales) {

        String email = credenciales.get("email");
        String password = credenciales.get("password");

        log.info("Intento de login para: {}", email);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getPassword().equals(password)) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        log.info("Login exitoso para: {} con rol: {}",
                email, usuario.getRol());

        return ResponseEntity.ok(Map.of(
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "rol", usuario.getRol().name()
        ));
    }
}