package microservice.necesidades.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.necesidades.model.Necesidad;
import microservice.necesidades.repository.NecesidadRepository;

@Service
@Transactional
public class NecesidadService {

    @Autowired
    private NecesidadRepository necesidadRepository;

    public List<Necesidad> obtenerNecesidades() {
        return necesidadRepository.findAll();
    }

    public Necesidad obtenerNecesidadPorId(Integer id) {
        return necesidadRepository.findById(id).orElse(null);
    }

    public Necesidad guardarNecesidad(Necesidad necesidad) {
        necesidad.setFechaReporte(LocalDateTime.now());

        // si viene null, inicializamos
        if (necesidad.getCantidadCubierta() == null) {
            necesidad.setCantidadCubierta(java.math.BigDecimal.ZERO);
        }

        return necesidadRepository.save(necesidad);
    }

    public Necesidad actualizarNecesidad(Integer id, Necesidad necesidad) {
        Necesidad existente = necesidadRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setTipoRecursoId(necesidad.getTipoRecursoId());
            existente.setCantidad(necesidad.getCantidad());
            existente.setCantidadCubierta(necesidad.getCantidadCubierta());
            existente.setDescripcion(necesidad.getDescripcion());
            existente.setDireccion(necesidad.getDireccion());
            existente.setComunaId(necesidad.getComunaId());
            existente.setEstado(necesidad.getEstado());
            existente.setReportadoPorUid(necesidad.getReportadoPorUid());

            return necesidadRepository.save(existente);
        }

        return null;
    }

    public Necesidad actualizarNecesidadParcial(Integer id, Necesidad necesidad) {
        Necesidad existente = necesidadRepository.findById(id).orElse(null);

        if (existente != null) {

            if (necesidad.getTipoRecursoId() != null)
                existente.setTipoRecursoId(necesidad.getTipoRecursoId());

            if (necesidad.getCantidad() != null)
                existente.setCantidad(necesidad.getCantidad());

            if (necesidad.getCantidadCubierta() != null)
                existente.setCantidadCubierta(necesidad.getCantidadCubierta());

            if (necesidad.getDescripcion() != null)
                existente.setDescripcion(necesidad.getDescripcion());

            if (necesidad.getDireccion() != null)
                existente.setDireccion(necesidad.getDireccion());

            if (necesidad.getComunaId() != null)
                existente.setComunaId(necesidad.getComunaId());

            if (necesidad.getEstado() != null)
                existente.setEstado(necesidad.getEstado());

            if (necesidad.getReportadoPorUid() != null)
                existente.setReportadoPorUid(necesidad.getReportadoPorUid());

            return necesidadRepository.save(existente);
        }

        return null;
    }

    public void eliminarNecesidad(Integer id) {
        Necesidad necesidad = necesidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Necesidad no encontrada"));

        necesidadRepository.delete(necesidad);
    }

    public List<Necesidad> obtenerPorEstado(Integer estadoId) {
        return necesidadRepository.findByEstadoId(estadoId);
    }

    public List<Necesidad> obtenerPorReportador(String uid) {
        return necesidadRepository.findByReportadoPorUid(uid);
    }

    public List<Necesidad> obtenerPorTipoRecurso(Integer tipoRecursoId) {
        return necesidadRepository.findByTipoRecursoId(tipoRecursoId);
    }

    public List<Necesidad> obtenerPorComuna(Integer comunaId) {
        return necesidadRepository.findByComunaId(comunaId);
    }
    public List<Necesidad> obtenerPorComunasIn(List<Integer> comunaIds) {
    return necesidadRepository.findByComunaIdIn(comunaIds);
    }
    public List<Necesidad> obtenerPorCentro(Integer centroAcopioId) {
    return necesidadRepository.findByCentroAcopioId(centroAcopioId);
    }
}