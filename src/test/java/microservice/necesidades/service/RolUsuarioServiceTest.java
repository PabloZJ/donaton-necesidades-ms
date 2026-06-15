package microservice.necesidades.service;

import microservice.necesidades.model.RolUsuario;
import microservice.necesidades.repository.RolUsuarioRepository;
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
@DisplayName("RolUsuarioService - Tests unitarios")
class RolUsuarioServiceTest {

    @Mock
    private RolUsuarioRepository repository;

    @InjectMocks
    private RolUsuarioService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private RolUsuario rolEjemplo() {
        return new RolUsuario(1, "ADMIN");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerRoles()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerRoles()")
    class ObtenerRoles {

        @Test
        @DisplayName("retorna lista con todos los roles")
        void deberiaRetornarTodosLosRoles() {
            List<RolUsuario> lista = List.of(
                new RolUsuario(1, "ADMIN"),
                new RolUsuario(2, "VOLUNTARIO")
            );
            when(repository.findAll()).thenReturn(lista);

            List<RolUsuario> resultado = service.obtenerRoles();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNombre()).isEqualTo("ADMIN");
            assertThat(resultado.get(1).getNombre()).isEqualTo("VOLUNTARIO");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay roles")
        void deberiaRetornarListaVaciaCuandoNoHayRoles() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<RolUsuario> resultado = service.obtenerRoles();

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerRolPorId()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerRolPorId()")
    class ObtenerRolPorId {

        @Test
        @DisplayName("retorna el rol cuando existe")
        void deberiaRetornarRolCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(rolEjemplo()));

            RolUsuario resultado = service.obtenerRolPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            RolUsuario resultado = service.obtenerRolPorId(99);

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // guardarRol()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("guardarRol()")
    class GuardarRol {

        @Test
        @DisplayName("guarda y retorna el rol con id asignado")
        void deberiaGuardarYRetornarRolConId() {
            RolUsuario nuevo = new RolUsuario(null, "DONANTE");
            RolUsuario guardado = new RolUsuario(3, "DONANTE");

            when(repository.save(nuevo)).thenReturn(guardado);

            RolUsuario resultado = service.guardarRol(nuevo);

            assertThat(resultado.getId()).isEqualTo(3);
            assertThat(resultado.getNombre()).isEqualTo("DONANTE");
            verify(repository, times(1)).save(nuevo);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarRol()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarRol()")
    class ActualizarRol {

        @Test
        @DisplayName("actualiza el nombre cuando el rol existe")
        void deberiaActualizarNombreCuandoExiste() {
            RolUsuario existente = new RolUsuario(1, "ADMIN");
            RolUsuario actualizado = new RolUsuario(1, "SUPERADMIN");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(RolUsuario.class))).thenReturn(actualizado);

            RolUsuario resultado = service.actualizarRol(1, new RolUsuario(null, "SUPERADMIN"));

            assertThat(resultado.getNombre()).isEqualTo("SUPERADMIN");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el rol no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            RolUsuario resultado = service.actualizarRol(99, new RolUsuario(null, "SUPERADMIN"));

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarRolParcial()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarRolParcial()")
    class ActualizarRolParcial {

        @Test
        @DisplayName("actualiza el nombre cuando se envía")
        void deberiaActualizarNombreCuandoSeEnvia() {
            RolUsuario existente = new RolUsuario(1, "ADMIN");
            RolUsuario parcial = new RolUsuario(null, "MODERADOR");
            RolUsuario esperado = new RolUsuario(1, "MODERADOR");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(RolUsuario.class))).thenReturn(esperado);

            RolUsuario resultado = service.actualizarRolParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("MODERADOR");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("no modifica el nombre cuando se envía null")
        void deberiaManternerNombreCuandoSeEnviaNulo() {
            RolUsuario existente = new RolUsuario(1, "ADMIN");
            RolUsuario parcial = new RolUsuario(null, null);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(RolUsuario.class))).thenReturn(existente);

            RolUsuario resultado = service.actualizarRolParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("retorna null cuando el rol no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            RolUsuario resultado = service.actualizarRolParcial(99, new RolUsuario(null, "X"));

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // eliminarRol()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("eliminarRol()")
    class EliminarRol {

        @Test
        @DisplayName("elimina el rol cuando existe")
        void deberiaEliminarCuandoExiste() {
            RolUsuario existente = rolEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminarRol(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el rol no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminarRol(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Rol no encontrado");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorNombre()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorNombre()")
    class ObtenerPorNombre {

        @Test
        @DisplayName("retorna el rol cuando el nombre existe")
        void deberiaRetornarRolCuandoNombreExiste() {
            when(repository.findByNombre("ADMIN")).thenReturn(rolEjemplo());

            RolUsuario resultado = service.obtenerPorNombre("ADMIN");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getNombre()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("retorna null cuando el nombre no existe")
        void deberiaRetornarNullCuandoNombreNoExiste() {
            when(repository.findByNombre("INEXISTENTE")).thenReturn(null);

            RolUsuario resultado = service.obtenerPorNombre("INEXISTENTE");

            assertThat(resultado).isNull();
        }
    }
}