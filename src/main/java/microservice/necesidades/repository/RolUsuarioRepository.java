package microservice.necesidades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import microservice.necesidades.model.RolUsuario;

@Repository
public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {
    RolUsuario findByNombre(String nombre);
}