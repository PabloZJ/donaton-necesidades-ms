package microservice.necesidades.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import microservice.necesidades.model.Necesidad;

@Repository
public interface NecesidadRepository extends JpaRepository<Necesidad, Integer> {

    List<Necesidad> findByEstadoId(Integer estadoId);

    List<Necesidad> findByReportadoPorUid(String uid);

    List<Necesidad> findByTipoRecursoId(Integer tipoRecursoId);

    List<Necesidad> findByComunaId(Integer comunaId);
}