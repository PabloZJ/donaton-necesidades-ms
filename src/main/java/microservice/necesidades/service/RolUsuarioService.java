package microservice.necesidades.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import microservice.necesidades.model.RolUsuario;
import microservice.necesidades.repository.RolUsuarioRepository;

@Service
@Transactional
public class RolUsuarioService {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    // Obtener todos
    public List<RolUsuario> obtenerRoles() {
        return rolUsuarioRepository.findAll();
    }

    // Obtener por ID
    public RolUsuario obtenerRolPorId(Integer id) {
        return rolUsuarioRepository.findById(id).orElse(null);
    }

    // Guardar
    public RolUsuario guardarRol(RolUsuario rol) {
        return rolUsuarioRepository.save(rol);
    }

    // Actualizar completo (PUT)
    public RolUsuario actualizarRol(Integer id, RolUsuario rol) {
        RolUsuario existente = rolUsuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(rol.getNombre());
            return rolUsuarioRepository.save(existente);
        }

        return null;
    }

    // Actualizar parcial (PATCH)
    public RolUsuario actualizarRolParcial(Integer id, RolUsuario rol) {
        RolUsuario existente = rolUsuarioRepository.findById(id).orElse(null);

        if (existente != null) {
            if (rol.getNombre() != null)
                existente.setNombre(rol.getNombre());

            return rolUsuarioRepository.save(existente);
        }

        return null;
    }

    // Eliminar
    public void eliminarRol(Integer id) {
        RolUsuario rol = rolUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        rolUsuarioRepository.delete(rol);
    }

    // Filtros
    public RolUsuario obtenerPorNombre(String nombre) {
        return rolUsuarioRepository.findByNombre(nombre);
    }
}