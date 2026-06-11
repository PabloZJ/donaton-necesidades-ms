package microservice.necesidades.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

import microservice.necesidades.model.Necesidad;
import microservice.necesidades.repository.NecesidadRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("NecesidadService - Tests unitarios")
class NecesidadServiceTest {

    @Mock
    private NecesidadRepository repository;

    @InjectMocks
    private NecesidadService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private Necesidad necesidadEjemplo() {
        Necesidad n = new Necesidad();
        n.setId(1);
        n.setTipoRecursoId(2);
        n.setCantidad(new BigDecimal("10"));
        n.setCantidadCubierta(new BigDecimal("0"));
        n.setDescripcion("Agua potable");
        n.setDireccion("Av. Siempreviva 123");
        n.setComunaId(5);
        n.setEstado(new microservice.necesidades.model.EstadoNecesidad(1, "Pendiente"));
        n.setReportadoPorUid("uid-123");
        n.setFechaReporte(LocalDateTime.now());
        return n;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerNecesidades()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerNecesidades()")
    class ObtenerNecesidades {

        @Test
        @DisplayName("retorna lista con todas las necesidades")
        void deberiaRetornarTodasLasNecesidades() {
            List<Necesidad> lista = List.of(necesidadEjemplo(), necesidadEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerNecesidades();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay necesidades")
        void deberiaRetornarListaVaciaCuandoNoHayNecesidades() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerNecesidades();

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerNecesidadPorId()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerNecesidadPorId()")
    class ObtenerNecesidadPorId {

        @Test
        @DisplayName("retorna la necesidad cuando existe")
        void deberiaRetornarNecesidadCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(necesidadEjemplo()));

            Necesidad resultado = service.obtenerNecesidadPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getDescripcion()).isEqualTo("Agua potable");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Necesidad resultado = service.obtenerNecesidadPorId(99);

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // guardarNecesidad()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("guardarNecesidad()")
    class GuardarNecesidad {

        @Test
        @DisplayName("asigna fechaReporte automáticamente al guardar")
        void deberiaAsignarFechaReporteAlGuardar() {
            Necesidad nueva = new Necesidad();
            nueva.setDescripcion("Ropa de abrigo");
            nueva.setCantidadCubierta(new BigDecimal("5"));

            when(repository.save(any(Necesidad.class))).thenAnswer(inv -> inv.getArgument(0));

            Necesidad resultado = service.guardarNecesidad(nueva);

            assertThat(resultado.getFechaReporte()).isNotNull();
            verify(repository, times(1)).save(nueva);
        }

        @Test
        @DisplayName("inicializa cantidadCubierta en cero cuando viene null")
        void deberiaInicializarCantidadCubiertaEnCeroSiEsNull() {
            Necesidad nueva = new Necesidad();
            nueva.setDescripcion("Medicamentos");
            nueva.setCantidadCubierta(null);

            when(repository.save(any(Necesidad.class))).thenAnswer(inv -> inv.getArgument(0));

            Necesidad resultado = service.guardarNecesidad(nueva);

            assertThat(resultado.getCantidadCubierta()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("no sobreescribe cantidadCubierta si ya viene con valor")
        void deberiaManternerCantidadCubiertaSiYaTieneValor() {
            Necesidad nueva = new Necesidad();
            nueva.setCantidadCubierta(new BigDecimal("3"));

            when(repository.save(any(Necesidad.class))).thenAnswer(inv -> inv.getArgument(0));

            Necesidad resultado = service.guardarNecesidad(nueva);

            assertThat(resultado.getCantidadCubierta()).isEqualByComparingTo(new BigDecimal("3"));
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarNecesidad()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarNecesidad()")
    class ActualizarNecesidad {

        @Test
        @DisplayName("actualiza todos los campos cuando la necesidad existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            Necesidad existente = necesidadEjemplo();
            Necesidad nuevaData = new Necesidad();
            nuevaData.setTipoRecursoId(3);
            nuevaData.setCantidad(new BigDecimal("20"));
            nuevaData.setCantidadCubierta(new BigDecimal("5"));
            nuevaData.setDescripcion("Alimentos no perecibles");
            nuevaData.setDireccion("Calle Nueva 456");
            nuevaData.setComunaId(7);
            nuevaData.setEstado(new microservice.necesidades.model.EstadoNecesidad(2, "En proceso"));
            nuevaData.setReportadoPorUid("uid-456");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Necesidad.class))).thenAnswer(inv -> inv.getArgument(0));

            Necesidad resultado = service.actualizarNecesidad(1, nuevaData);

            assertThat(resultado.getTipoRecursoId()).isEqualTo(3);
            assertThat(resultado.getCantidad()).isEqualByComparingTo(new BigDecimal("20"));
            assertThat(resultado.getDescripcion()).isEqualTo("Alimentos no perecibles");
            assertThat(resultado.getDireccion()).isEqualTo("Calle Nueva 456");
            assertThat(resultado.getComunaId()).isEqualTo(7);
            assertThat(resultado.getReportadoPorUid()).isEqualTo("uid-456");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando la necesidad no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Necesidad resultado = service.actualizarNecesidad(99, new Necesidad());

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarNecesidadParcial()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarNecesidadParcial()")
    class ActualizarNecesidadParcial {

        @Test
        @DisplayName("actualiza solo los campos no nulos")
        void deberiaActualizarSoloCamposNoNulos() {
            Necesidad existente = necesidadEjemplo();
            Necesidad parcial = new Necesidad();
            parcial.setDescripcion("Nueva descripción");
            // el resto null → no deben cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Necesidad.class))).thenAnswer(inv -> inv.getArgument(0));

            Necesidad resultado = service.actualizarNecesidadParcial(1, parcial);

            assertThat(resultado.getDescripcion()).isEqualTo("Nueva descripción");
            assertThat(resultado.getDireccion()).isEqualTo("Av. Siempreviva 123"); // sin cambio
            assertThat(resultado.getComunaId()).isEqualTo(5);                       // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            Necesidad existente = necesidadEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Necesidad.class))).thenAnswer(inv -> inv.getArgument(0));

            Necesidad resultado = service.actualizarNecesidadParcial(1, new Necesidad());

            assertThat(resultado.getDescripcion()).isEqualTo("Agua potable");
            assertThat(resultado.getDireccion()).isEqualTo("Av. Siempreviva 123");
        }

        @Test
        @DisplayName("retorna null cuando la necesidad no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Necesidad resultado = service.actualizarNecesidadParcial(99, new Necesidad());

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // eliminarNecesidad()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("eliminarNecesidad()")
    class EliminarNecesidad {

        @Test
        @DisplayName("elimina la necesidad cuando existe")
        void deberiaEliminarCuandoExiste() {
            Necesidad existente = necesidadEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminarNecesidad(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando la necesidad no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminarNecesidad(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Necesidad no encontrada");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorEstado()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorEstado()")
    class ObtenerPorEstado {

        @Test
        @DisplayName("retorna necesidades filtradas por estado")
        void deberiaRetornarNecesidadesPorEstado() {
            List<Necesidad> lista = List.of(necesidadEjemplo());
            when(repository.findByEstadoId(1)).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerPorEstado(1);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByEstadoId(1);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay necesidades con ese estado")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByEstadoId(99)).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerPorEstado(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorReportador()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorReportador()")
    class ObtenerPorReportador {

        @Test
        @DisplayName("retorna necesidades filtradas por uid del reportador")
        void deberiaRetornarNecesidadesPorReportador() {
            List<Necesidad> lista = List.of(necesidadEjemplo());
            when(repository.findByReportadoPorUid("uid-123")).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerPorReportador("uid-123");

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getReportadoPorUid()).isEqualTo("uid-123");
        }

        @Test
        @DisplayName("retorna lista vacía cuando el uid no tiene necesidades")
        void deberiaRetornarListaVaciaCuandoUidNoTieneNecesidades() {
            when(repository.findByReportadoPorUid("uid-999")).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerPorReportador("uid-999");

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorTipoRecurso()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorTipoRecurso()")
    class ObtenerPorTipoRecurso {

        @Test
        @DisplayName("retorna necesidades filtradas por tipo de recurso")
        void deberiaRetornarNecesidadesPorTipoRecurso() {
            List<Necesidad> lista = List.of(necesidadEjemplo());
            when(repository.findByTipoRecursoId(2)).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerPorTipoRecurso(2);

            assertThat(resultado).hasSize(1);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay necesidades con ese tipo")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByTipoRecursoId(99)).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerPorTipoRecurso(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorComuna()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorComuna()")
    class ObtenerPorComuna {

        @Test
        @DisplayName("retorna necesidades filtradas por comuna")
        void deberiaRetornarNecesidadesPorComuna() {
            List<Necesidad> lista = List.of(necesidadEjemplo());
            when(repository.findByComunaId(5)).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerPorComuna(5);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getComunaId()).isEqualTo(5);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay necesidades en esa comuna")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByComunaId(99)).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerPorComuna(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorComunasIn()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorComunasIn()")
    class ObtenerPorComunasIn {

        @Test
        @DisplayName("retorna necesidades de múltiples comunas")
        void deberiaRetornarNecesidadesDeMultiplesComunas() {
            List<Integer> comunas = List.of(5, 7, 9);
            List<Necesidad> lista = List.of(necesidadEjemplo(), necesidadEjemplo());
            when(repository.findByComunaIdIn(comunas)).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerPorComunasIn(comunas);

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findByComunaIdIn(comunas);
        }

        @Test
        @DisplayName("retorna lista vacía cuando ninguna comuna tiene necesidades")
        void deberiaRetornarListaVaciaCuandoNingunaComuna() {
            List<Integer> comunas = List.of(98, 99);
            when(repository.findByComunaIdIn(comunas)).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerPorComunasIn(comunas);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorCentro()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorCentro()")
    class ObtenerPorCentro {

        @Test
        @DisplayName("retorna necesidades filtradas por centro de acopio")
        void deberiaRetornarNecesidadesPorCentro() {
            List<Necesidad> lista = List.of(necesidadEjemplo());
            when(repository.findByCentroAcopioId(10)).thenReturn(lista);

            List<Necesidad> resultado = service.obtenerPorCentro(10);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByCentroAcopioId(10);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay necesidades en ese centro")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByCentroAcopioId(99)).thenReturn(Collections.emptyList());

            List<Necesidad> resultado = service.obtenerPorCentro(99);

            assertThat(resultado).isEmpty();
        }
    }
}