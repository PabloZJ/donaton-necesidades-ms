package microservice.necesidades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import microservice.necesidades.model.EstadoNecesidad;

@Repository
public interface EstadoNecesidadRepository extends JpaRepository<EstadoNecesidad, Integer> {
    EstadoNecesidad findByNombre(String nombre);
}