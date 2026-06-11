package microservice.necesidades.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservice.necesidades.model.EstadoNecesidad;
import microservice.necesidades.service.EstadoNecesidadService;

@WebMvcTest(EstadoNecesidadController.class)
@DisplayName("EstadoNecesidadController - Tests de integración")
class EstadoNecesidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Instanciado directamente, sin depender del contexto de Spring
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private EstadoNecesidadService service;

    private EstadoNecesidad estadoEjemplo() {
        return new EstadoNecesidad(1, "Pendiente");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // GET /estados-necesidad
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /estados-necesidad")
    class ObtenerTodos {

        @Test
        @DisplayName("200 OK - retorna lista de estados")
        void deberiaRetornarListaDeEstados() throws Exception {
            List<EstadoNecesidad> lista = List.of(
                new EstadoNecesidad(1, "Pendiente"),
                new EstadoNecesidad(2, "En proceso")
            );
            when(service.obtenerEstados()).thenReturn(lista);

            mockMvc.perform(get("/estados-necesidad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Pendiente"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("En proceso"));
        }

        @Test
        @DisplayName("204 No Content - lista vacía")
        void deberiaRetornarNoContentCuandoListaEstaVacia() throws Exception {
            when(service.obtenerEstados()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/estados-necesidad"))
                .andExpect(status().isNoContent());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // GET /estados-necesidad/{id}
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /estados-necesidad/{id}")
    class ObtenerPorId {

        @Test
        @DisplayName("200 OK - estado encontrado")
        void deberiaRetornarEstadoCuandoExiste() throws Exception {
            when(service.obtenerEstadoPorId(1)).thenReturn(estadoEjemplo());

            mockMvc.perform(get("/estados-necesidad/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Pendiente"));
        }

        @Test
        @DisplayName("404 Not Found - estado no encontrado")
        void deberiaRetornarNotFoundCuandoNoExiste() throws Exception {
            when(service.obtenerEstadoPorId(99)).thenReturn(null);

            mockMvc.perform(get("/estados-necesidad/99"))
                .andExpect(status().isNotFound());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // POST /estados-necesidad
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("POST /estados-necesidad")
    class Crear {

        @Test
        @DisplayName("201 Created - estado creado correctamente")
        void deberiaCrearNuevoEstado() throws Exception {
            EstadoNecesidad nuevo = new EstadoNecesidad(null, "Finalizado");
            EstadoNecesidad guardado = new EstadoNecesidad(3, "Finalizado");

            when(service.guardarEstado(any(EstadoNecesidad.class))).thenReturn(guardado);

            mockMvc.perform(post("/estados-necesidad")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Finalizado"));
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PUT /estados-necesidad/{id}
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("PUT /estados-necesidad/{id}")
    class Actualizar {

        @Test
        @DisplayName("200 OK - estado actualizado correctamente")
        void deberiaActualizarEstadoExistente() throws Exception {
            EstadoNecesidad actualizado = new EstadoNecesidad(1, "Aprobado");
            when(service.actualizarEstado(eq(1), any(EstadoNecesidad.class))).thenReturn(actualizado);

            mockMvc.perform(put("/estados-necesidad/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Aprobado"));
        }

        @Test
        @DisplayName("404 Not Found - estado a actualizar no existe")
        void deberiaRetornarNotFoundCuandoEstadoNoExiste() throws Exception {
            when(service.actualizarEstado(eq(99), any(EstadoNecesidad.class))).thenReturn(null);

            mockMvc.perform(put("/estados-necesidad/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EstadoNecesidad(null, "X"))))
                .andExpect(status().isNotFound());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PATCH /estados-necesidad/{id}
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("PATCH /estados-necesidad/{id}")
    class ActualizarParcial {

        @Test
        @DisplayName("200 OK - actualización parcial exitosa")
        void deberiaActualizarParcialmenteEstadoExistente() throws Exception {
            EstadoNecesidad parcial = new EstadoNecesidad(null, "Cancelado");
            EstadoNecesidad resultado = new EstadoNecesidad(1, "Cancelado");

            when(service.actualizarEstadoParcial(eq(1), any(EstadoNecesidad.class))).thenReturn(resultado);

            mockMvc.perform(patch("/estados-necesidad/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(parcial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Cancelado"));
        }

        @Test
        @DisplayName("404 Not Found - estado a actualizar parcialmente no existe")
        void deberiaRetornarNotFoundEnActualizacionParcialCuandoNoExiste() throws Exception {
            when(service.actualizarEstadoParcial(eq(99), any(EstadoNecesidad.class))).thenReturn(null);

            mockMvc.perform(patch("/estados-necesidad/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new EstadoNecesidad(null, "X"))))
                .andExpect(status().isNotFound());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DELETE /estados-necesidad/{id}
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("DELETE /estados-necesidad/{id}")
    class Eliminar {

        @Test
        @DisplayName("204 No Content - estado eliminado correctamente")
        void deberiaEliminarEstado() throws Exception {
            doNothing().when(service).eliminarEstado(1);

            mockMvc.perform(delete("/estados-necesidad/1"))
                .andExpect(status().isNoContent());

            verify(service, times(1)).eliminarEstado(1);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // GET /estados-necesidad/nombre/{nombre}
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /estados-necesidad/nombre/{nombre}")
    class ObtenerPorNombre {

        @Test
        @DisplayName("200 OK - estado encontrado por nombre")
        void deberiaRetornarEstadoCuandoNombreExiste() throws Exception {
            when(service.obtenerPorNombre("Pendiente")).thenReturn(estadoEjemplo());

            mockMvc.perform(get("/estados-necesidad/nombre/Pendiente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Pendiente"));
        }

        @Test
        @DisplayName("404 Not Found - nombre no existe")
        void deberiaRetornarNotFoundCuandoNombreNoExiste() throws Exception {
            when(service.obtenerPorNombre("Inexistente")).thenReturn(null);

            mockMvc.perform(get("/estados-necesidad/nombre/Inexistente"))
                .andExpect(status().isNotFound());
        }
    }
}