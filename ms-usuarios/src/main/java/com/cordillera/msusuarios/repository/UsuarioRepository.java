package com.cordillera.msusuarios.repository;

import com.cordillera.msusuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * PATRÓN REPOSITORY - MS-Usuarios
 * =================================
 * Abstrae el acceso a la base de datos.
 * La lógica de negocio (Service) no necesita conocer
 * los detalles de cómo se almacenan los usuarios.
 *
 * Si se cambia MySQL por otro motor de BD,
 * solo se modifica esta capa sin afectar el Service.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuario por email para autenticación
    Optional<Usuario> findByEmail(String email);

    // Verifica si ya existe un email registrado
    boolean existsByEmail(String email);
}