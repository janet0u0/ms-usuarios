package com.cordillera.msusuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

/**
 * Entidad Usuario - MS-Usuarios
 * Representa un usuario del sistema con su rol (RBAC).
 * El password se oculta en las respuestas JSON con @JsonIgnore.
 *
 * Patrón aplicado: Repository Pattern
 * Esta entidad es gestionada por UsuarioRepository
 * que abstrae el acceso a la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(nullable = false)
    private LocalDateTime actualizadoEn;

    /**
     * Se ejecuta automáticamente antes de insertar.
     * Asigna fechas de creación y actualización.
     */
    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        this.actualizadoEn = this.creadoEn;
    }

    /**
     * Se ejecuta automáticamente antes de actualizar.
     * Actualiza la fecha de modificación.
     */
    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }
}