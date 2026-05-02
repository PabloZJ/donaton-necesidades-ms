package microservice.necesidades.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import microservice.necesidades.model.EstadoNecesidad;
import microservice.necesidades.repository.EstadoNecesidadRepository;

@Service
@Transactional
public class EstadoNecesidadService {

    @Autowired
    private EstadoNecesidadRepository estadoNecesidadRepository;

    public List<EstadoNecesidad> obtenerEstados() {
        return estadoNecesidadRepository.findAll();
    }

    public EstadoNecesidad obtenerEstadoPorId(Integer id) {
        return estadoNecesidadRepository.findById(id).orElse(null);
    }

    public EstadoNecesidad guardarEstado(EstadoNecesidad estado) {
        return estadoNecesidadRepository.save(estado);
    }

    public EstadoNecesidad actualizarEstado(Integer id, EstadoNecesidad estado) {
        EstadoNecesidad existente = estadoNecesidadRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(estado.getNombre());
            return estadoNecesidadRepository.save(existente);
        }

        return null;
    }

    public EstadoNecesidad actualizarEstadoParcial(Integer id, EstadoNecesidad estado) {
        EstadoNecesidad existente = estadoNecesidadRepository.findById(id).orElse(null);

        if (existente != null) {
            if (estado.getNombre() != null)
                existente.setNombre(estado.getNombre());

            return estadoNecesidadRepository.save(existente);
        }

        return null;
    }

    public void eliminarEstado(Integer id) {
        EstadoNecesidad estado = estadoNecesidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        estadoNecesidadRepository.delete(estado);
    }

    public EstadoNecesidad obtenerPorNombre(String nombre) {
        return estadoNecesidadRepository.findByNombre(nombre);
    }
}