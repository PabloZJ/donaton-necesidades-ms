package microservice.necesidades.service;

import microservice.necesidades.model.RolUsuario;
import microservice.necesidades.model.UsuarioPerfil;
import microservice.necesidades.repository.UsuarioPerfilRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioPerfilService - Tests unitarios")
class UsuarioPerfilServiceTest {

    @Mock
    private UsuarioPerfilRepository repository;

    @InjectMocks
    private UsuarioPerfilService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private UsuarioPerfil usuarioEjemplo() {
        return new UsuarioPerfil(1, "uid123", "Juan Pérez", "juan@test.com", new RolUsuario(1, "donante"), null, null);
    }

    private UsuarioPerfil operadorEjemplo() {
        return new UsuarioPerfil(2, "uid456", "Pedro Operador", "pedro@test.com", new RolUsuario(3, "operador"), 1, null);
    }

    private UsuarioPerfil municipalidadEjemplo() {
        return new UsuarioPerfil(3, "uid789", "Muni Puente Alto", "muni@test.com", new RolUsuario(2, "municipalidad"), null, 13114);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerUsuarios()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerUsuarios()")
    class ObtenerUsuarios {

        @Test
        @DisplayName("retorna lista con todos los usuarios")
        void deberiaRetornarTodosLosUsuarios() {
            List<UsuarioPerfil> lista = List.of(usuarioEjemplo(), operadorEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<UsuarioPerfil> resultado = service.obtenerUsuarios();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Juan Pérez");
            assertThat(resultado.get(1).getNombre()).isEqualTo("Pedro Operador");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay usuarios")
        void deberiaRetornarListaVaciaCuandoNoHayUsuarios() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<UsuarioPerfil> resultado = service.obtenerUsuarios();

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerUsuarioPorId()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerUsuarioPorId()")
    class ObtenerUsuarioPorId {

        @Test
        @DisplayName("retorna el usuario cuando existe")
        void deberiaRetornarUsuarioCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(usuarioEjemplo()));

            UsuarioPerfil resultado = service.obtenerUsuarioPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("Juan Pérez");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            UsuarioPerfil resultado = service.obtenerUsuarioPorId(99);

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // guardarUsuario()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("guardarUsuario()")
    class GuardarUsuario {

        @Test
        @DisplayName("guarda y retorna el usuario con id asignado")
        void deberiaGuardarYRetornarUsuarioConId() {
            UsuarioPerfil nuevo = new UsuarioPerfil(null, "uid999", "Nuevo Usuario", "nuevo@test.com", new RolUsuario(1, "donante"), null, null);
            UsuarioPerfil guardado = new UsuarioPerfil(5, "uid999", "Nuevo Usuario", "nuevo@test.com", new RolUsuario(1, "donante"), null, null);

            when(repository.save(nuevo)).thenReturn(guardado);

            UsuarioPerfil resultado = service.guardarUsuario(nuevo);

            assertThat(resultado.getId()).isEqualTo(5);
            assertThat(resultado.getNombre()).isEqualTo("Nuevo Usuario");
            verify(repository, times(1)).save(nuevo);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarUsuario()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarUsuario()")
    class ActualizarUsuario {

        @Test
        @DisplayName("actualiza todos los campos cuando el usuario existe")
        void deberiaActualizarTodosLosCamposCuandoExiste() {
            UsuarioPerfil existente = usuarioEjemplo();
            UsuarioPerfil actualizado = new UsuarioPerfil(null, "uid999", "Juan Actualizado", "juan2@test.com", new RolUsuario(2, "municipalidad"), null, 13114);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(UsuarioPerfil.class))).thenReturn(existente);

            UsuarioPerfil resultado = service.actualizarUsuario(1, actualizado);

            assertThat(resultado.getNombre()).isEqualTo("Juan Actualizado");
            assertThat(resultado.getEmail()).isEqualTo("juan2@test.com");
            assertThat(resultado.getComunaId()).isEqualTo(13114);
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el usuario no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            UsuarioPerfil resultado = service.actualizarUsuario(99, usuarioEjemplo());

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarUsuarioParcial()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarUsuarioParcial()")
    class ActualizarUsuarioParcial {

        @Test
        @DisplayName("actualiza solo el nombre cuando se envía")
        void deberiaActualizarSoloNombreCuandoSeEnvia() {
            UsuarioPerfil existente = usuarioEjemplo();
            UsuarioPerfil parcial = new UsuarioPerfil(null, null, "Solo Nombre", null, null, null, null);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(UsuarioPerfil.class))).thenReturn(existente);

            UsuarioPerfil resultado = service.actualizarUsuarioParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("Solo Nombre");
            assertThat(resultado.getEmail()).isEqualTo("juan@test.com");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("actualiza centroAcopioId para operador")
        void deberiaActualizarCentroAcopioId() {
            UsuarioPerfil existente = operadorEjemplo();
            UsuarioPerfil parcial = new UsuarioPerfil(null, null, null, null, null, 5, null);

            when(repository.findById(2)).thenReturn(Optional.of(existente));
            when(repository.save(any(UsuarioPerfil.class))).thenReturn(existente);

            UsuarioPerfil resultado = service.actualizarUsuarioParcial(2, parcial);

            assertThat(resultado.getCentroAcopioId()).isEqualTo(5);
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el usuario no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            UsuarioPerfil resultado = service.actualizarUsuarioParcial(99, usuarioEjemplo());

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // eliminarUsuario()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("eliminarUsuario()")
    class EliminarUsuario {

        @Test
        @DisplayName("elimina el usuario cuando existe")
        void deberiaEliminarCuandoExiste() {
            UsuarioPerfil existente = usuarioEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminarUsuario(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el usuario no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminarUsuario(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuario no encontrado");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorFirebaseUid()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorFirebaseUid()")
    class ObtenerPorFirebaseUid {

        @Test
        @DisplayName("retorna el usuario cuando el uid existe")
        void deberiaRetornarUsuarioCuandoUidExiste() {
            when(repository.findByFirebaseUid("uid123")).thenReturn(usuarioEjemplo());

            UsuarioPerfil resultado = service.obtenerPorFirebaseUid("uid123");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getFirebaseUid()).isEqualTo("uid123");
        }

        @Test
        @DisplayName("retorna null cuando el uid no existe")
        void deberiaRetornarNullCuandoUidNoExiste() {
            when(repository.findByFirebaseUid("inexistente")).thenReturn(null);

            UsuarioPerfil resultado = service.obtenerPorFirebaseUid("inexistente");

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorEmail()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorEmail()")
    class ObtenerPorEmail {

        @Test
        @DisplayName("retorna el usuario cuando el email existe")
        void deberiaRetornarUsuarioCuandoEmailExiste() {
            when(repository.findByEmail("juan@test.com")).thenReturn(usuarioEjemplo());

            UsuarioPerfil resultado = service.obtenerPorEmail("juan@test.com");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getEmail()).isEqualTo("juan@test.com");
        }

        @Test
        @DisplayName("retorna null cuando el email no existe")
        void deberiaRetornarNullCuandoEmailNoExiste() {
            when(repository.findByEmail("noexiste@test.com")).thenReturn(null);

            UsuarioPerfil resultado = service.obtenerPorEmail("noexiste@test.com");

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorRol()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorRol()")
    class ObtenerPorRol {

        @Test
        @DisplayName("retorna usuarios con el rol especificado")
        void deberiaRetornarUsuariosPorRol() {
            when(repository.findByRolId(3)).thenReturn(List.of(operadorEjemplo()));

            List<UsuarioPerfil> resultado = service.obtenerPorRol(3);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getRol().getNombre()).isEqualTo("operador");
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay usuarios con ese rol")
        void deberiaRetornarListaVaciaCuandoNoHayRol() {
            when(repository.findByRolId(99)).thenReturn(Collections.emptyList());

            List<UsuarioPerfil> resultado = service.obtenerPorRol(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorCentroAcopio()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorCentroAcopio()")
    class ObtenerPorCentroAcopio {

        @Test
        @DisplayName("retorna operadores del centro de acopio")
        void deberiaRetornarOperadoresPorCentro() {
            when(repository.findByCentroAcopioId(1)).thenReturn(List.of(operadorEjemplo()));

            List<UsuarioPerfil> resultado = service.obtenerPorCentroAcopio(1);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getCentroAcopioId()).isEqualTo(1);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorComuna()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorComuna()")
    class ObtenerPorComuna {

        @Test
        @DisplayName("retorna municipalidades de la comuna")
        void deberiaRetornarMunicipalidadesPorComuna() {
            when(repository.findByComunaId(13114)).thenReturn(List.of(municipalidadEjemplo()));

            List<UsuarioPerfil> resultado = service.obtenerPorComuna(13114);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getComunaId()).isEqualTo(13114);
        }
    }
}