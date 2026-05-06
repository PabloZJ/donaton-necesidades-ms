package microservice.necesidades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservice.necesidades.model.UsuarioPerfil;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Integer> {
    UsuarioPerfil findByFirebaseUid(String firebaseUid);
    UsuarioPerfil findByEmail(String email);
    List<UsuarioPerfil> findByRolId(Integer rolId);
    List<UsuarioPerfil> findByCentroAcopioId(Integer centroAcopioId);
    List<UsuarioPerfil> findByComunaId(Integer comunaId);
}