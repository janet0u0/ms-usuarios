package com.cordillera.msusuarios.controller;

import com.cordillera.msusuarios.dto.UsuarioRequestDTO;
import com.cordillera.msusuarios.dto.UsuarioResponseDTO;
import com.cordillera.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @RequestBody UsuarioRequestDTO dto) {

        log.info("Intento de login para: {}", dto.getEmail());
        UsuarioResponseDTO usuario = usuarioService.login(
            dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(usuario);
    }
}