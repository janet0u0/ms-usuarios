package com.cordillera.msusuarios.dto;

import com.cordillera.msusuarios.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO de salida para respuestas de Usuario.
 * Expone solo los campos necesarios al cliente.
 * NO expone el password por seguridad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private Rol rol;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}