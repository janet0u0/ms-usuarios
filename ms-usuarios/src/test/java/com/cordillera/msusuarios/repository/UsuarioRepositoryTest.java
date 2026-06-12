package com.cordillera.msusuarios.repository;

import com.cordillera.msusuarios.model.Rol;
import com.cordillera.msusuarios.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    @DisplayName("Debe guardar usuario correctamente")
    void guardar_CuandoUsuarioEsValido_DeberiaPersistirDatos() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Janet");
        usuario.setEmail("janet@test.com");
        usuario.setPassword("123");
        usuario.setRol(Rol.ADMIN_SISTEMA);

        Usuario resultado = repository.save(usuario);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Janet", resultado.getNombre());
        assertEquals("janet@test.com", resultado.getEmail());
    }

    @Test
    @DisplayName("Debe buscar usuario por email")
    void findByEmail_CuandoUsuarioExiste_DeberiaRetornarUsuario() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Janet");
        usuario.setEmail("janet@test.com");
        usuario.setPassword("123");
        usuario.setRol(Rol.ADMIN_SISTEMA);

        repository.save(usuario);

        Optional<Usuario> resultado =
                repository.findByEmail("janet@test.com");

        assertTrue(resultado.isPresent());
        assertEquals("Janet", resultado.get().getNombre());
        assertEquals("janet@test.com", resultado.get().getEmail());
    }

    @Test
    @DisplayName("Debe verificar si email existe")
    void existsByEmail_CuandoEmailExiste_DeberiaRetornarTrue() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Janet");
        usuario.setEmail("janet@test.com");
        usuario.setPassword("123");
        usuario.setRol(Rol.ADMIN_SISTEMA);

        repository.save(usuario);

        boolean existe =
                repository.existsByEmail("janet@test.com");

        assertTrue(existe);
    }

    @Test
    @DisplayName("Debe ejecutar preUpdate al modificar usuario")
    void preUpdate_CuandoSeActualiza_DeberiaActualizarFecha() {

        Usuario u = new Usuario();
        u.setNombre("Test");
        u.setEmail("test@test.com");
        u.setPassword("123");
        u.setRol(Rol.ANALISTA);

        u.prePersist();

        Usuario guardado = repository.save(u);

        guardado.setNombre("Test Actualizado");
        guardado.preUpdate();

        Usuario actualizado = repository.save(guardado);

        assertNotNull(actualizado.getActualizadoEn());
        assertEquals("Test Actualizado", actualizado.getNombre());
    }

    @Test
    @DisplayName("Debe ejecutar prePersist al crear usuario")
    void prePersist_CuandoSeCreaUsuario_DeberiaAsignarFechas() {

        Usuario usuario = new Usuario();
        usuario.setNombre("Janet");
        usuario.setEmail("janet2@test.com");
        usuario.setPassword("123");
        usuario.setRol(Rol.ADMIN_SISTEMA);

        usuario.prePersist();

        assertNotNull(usuario.getCreadoEn());
        assertNotNull(usuario.getActualizadoEn());
        assertEquals(
                usuario.getCreadoEn(),
                usuario.getActualizadoEn()
        );
    }
}