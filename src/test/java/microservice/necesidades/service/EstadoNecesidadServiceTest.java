package microservice.necesidades.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.necesidades.model.EstadoNecesidad;
import microservice.necesidades.repository.EstadoNecesidadRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("EstadoNecesidadService - Tests unitarios")
class EstadoNecesidadServiceTest {

    @Mock
    private EstadoNecesidadRepository repository;

    @InjectMocks
    private EstadoNecesidadService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private EstadoNecesidad estadoEjemplo() {
        return new EstadoNecesidad(1, "Pendiente");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerEstados()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerEstados()")
    class ObtenerEstados {

        @Test
        @DisplayName("retorna lista con todos los estados")
        void deberiaRetornarTodosLosEstados() {
            List<EstadoNecesidad> lista = List.of(
                new EstadoNecesidad(1, "Pendiente"),
                new EstadoNecesidad(2, "En proceso")
            );
            when(repository.findAll()).thenReturn(lista);

            List<EstadoNecesidad> resultado = service.obtenerEstados();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Pendiente");
            assertThat(resultado.get(1).getNombre()).isEqualTo("En proceso");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay estados")
        void deberiaRetornarListaVaciaCuandoNoHayEstados() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<EstadoNecesidad> resultado = service.obtenerEstados();

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerEstadoPorId()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerEstadoPorId()")
    class ObtenerEstadoPorId {

        @Test
        @DisplayName("retorna el estado cuando existe")
        void deberiaRetornarEstadoCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(estadoEjemplo()));

            EstadoNecesidad resultado = service.obtenerEstadoPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("Pendiente");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            EstadoNecesidad resultado = service.obtenerEstadoPorId(99);

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // guardarEstado()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("guardarEstado()")
    class GuardarEstado {

        @Test
        @DisplayName("guarda y retorna el estado con id asignado")
        void deberiaGuardarYRetornarEstadoConId() {
            EstadoNecesidad nuevo = new EstadoNecesidad(null, "Finalizado");
            EstadoNecesidad guardado = new EstadoNecesidad(3, "Finalizado");

            when(repository.save(nuevo)).thenReturn(guardado);

            EstadoNecesidad resultado = service.guardarEstado(nuevo);

            assertThat(resultado.getId()).isEqualTo(3);
            assertThat(resultado.getNombre()).isEqualTo("Finalizado");
            verify(repository, times(1)).save(nuevo);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarEstado()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarEstado()")
    class ActualizarEstado {

        @Test
        @DisplayName("actualiza el nombre cuando el estado existe")
        void deberiaActualizarNombreCuandoExiste() {
            EstadoNecesidad existente = new EstadoNecesidad(1, "Pendiente");
            EstadoNecesidad actualizado = new EstadoNecesidad(1, "Aprobado");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(EstadoNecesidad.class))).thenReturn(actualizado);

            EstadoNecesidad resultado = service.actualizarEstado(1, new EstadoNecesidad(null, "Aprobado"));

            assertThat(resultado.getNombre()).isEqualTo("Aprobado");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el estado no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            EstadoNecesidad resultado = service.actualizarEstado(99, new EstadoNecesidad(null, "Aprobado"));

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarEstadoParcial()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarEstadoParcial()")
    class ActualizarEstadoParcial {

        @Test
        @DisplayName("actualiza el nombre cuando se envía")
        void deberiaActualizarNombreCuandoSeEnvia() {
            EstadoNecesidad existente = new EstadoNecesidad(1, "Pendiente");
            EstadoNecesidad parcial = new EstadoNecesidad(null, "Cancelado");
            EstadoNecesidad resultado_esperado = new EstadoNecesidad(1, "Cancelado");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(EstadoNecesidad.class))).thenReturn(resultado_esperado);

            EstadoNecesidad resultado = service.actualizarEstadoParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("Cancelado");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("no modifica el nombre cuando se envía null")
        void deberiaManternerNombreCuandoSeEnviaNombre() {
            EstadoNecesidad existente = new EstadoNecesidad(1, "Pendiente");
            EstadoNecesidad parcial = new EstadoNecesidad(null, null);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(EstadoNecesidad.class))).thenReturn(existente);

            EstadoNecesidad resultado = service.actualizarEstadoParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("Pendiente");
        }

        @Test
        @DisplayName("retorna null cuando el estado no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            EstadoNecesidad resultado = service.actualizarEstadoParcial(99, new EstadoNecesidad(null, "X"));

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // eliminarEstado()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("eliminarEstado()")
    class EliminarEstado {

        @Test
        @DisplayName("elimina el estado cuando existe")
        void deberiaEliminarCuandoExiste() {
            EstadoNecesidad existente = estadoEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminarEstado(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el estado no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminarEstado(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Estado no encontrado");

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
        @DisplayName("retorna el estado cuando el nombre existe")
        void deberiaRetornarEstadoCuandoNombreExiste() {
            when(repository.findByNombre("Pendiente")).thenReturn(estadoEjemplo());

            EstadoNecesidad resultado = service.obtenerPorNombre("Pendiente");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getNombre()).isEqualTo("Pendiente");
        }

        @Test
        @DisplayName("retorna null cuando el nombre no existe")
        void deberiaRetornarNullCuandoNombreNoExiste() {
            when(repository.findByNombre("Inexistente")).thenReturn(null);

            EstadoNecesidad resultado = service.obtenerPorNombre("Inexistente");

            assertThat(resultado).isNull();
        }
    }
}