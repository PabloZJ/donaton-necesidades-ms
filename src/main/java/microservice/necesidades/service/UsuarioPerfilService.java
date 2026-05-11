package microservice.necesidades.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.necesidades.model.UsuarioPerfil;
import microservice.necesidades.repository.UsuarioPerfilRepository;

@Service
@Transactional
public class UsuarioPerfilService {

    @Autowired
    private UsuarioPerfilRepository usuarioPerfilRepository;

    public List<UsuarioPerfil> obtenerUsuarios() {
        return usuarioPerfilRepository.findAll();
    }

    public UsuarioPerfil obtenerUsuarioPorId(Integer id) {
        return usuarioPerfilRepository.findById(id).orElse(null);
    }

    public UsuarioPerfil guardarUsuario(UsuarioPerfil usuario) {
        return usuarioPerfilRepository.save(usuario);
    }

    public UsuarioPerfil actualizarUsuario(Integer id, UsuarioPerfil usuario) {
        UsuarioPerfil existente = usuarioPerfilRepository.findById(id).orElse(null);

        if (existente != null) {
            existente.setFirebaseUid(usuario.getFirebaseUid());
            existente.setNombre(usuario.getNombre());
            existente.setEmail(usuario.getEmail());
            existente.setRol(usuario.getRol());
            existente.setCentroAcopioId(usuario.getCentroAcopioId());
            existente.setComunaId(usuario.getComunaId());
            return usuarioPerfilRepository.save(existente);
        }

        return null;
    }

    public UsuarioPerfil actualizarUsuarioParcial(Integer id, UsuarioPerfil usuario) {
        UsuarioPerfil existente = usuarioPerfilRepository.findById(id).orElse(null);

        if (existente != null) {
            if (usuario.getFirebaseUid() != null)
                existente.setFirebaseUid(usuario.getFirebaseUid());

            if (usuario.getNombre() != null)
                existente.setNombre(usuario.getNombre());

            if (usuario.getEmail() != null)
                existente.setEmail(usuario.getEmail());

            if (usuario.getRol() != null)
                existente.setRol(usuario.getRol());

            if (usuario.getCentroAcopioId() != null)
                existente.setCentroAcopioId(usuario.getCentroAcopioId());

            if (usuario.getComunaId() != null)
                existente.setComunaId(usuario.getComunaId());

            return usuarioPerfilRepository.save(existente);
        }

        return null;
    }

    public void eliminarUsuario(Integer id) {
        UsuarioPerfil usuario = usuarioPerfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioPerfilRepository.delete(usuario);
    }

    public UsuarioPerfil obtenerPorFirebaseUid(String firebaseUid) {
        return usuarioPerfilRepository.findByFirebaseUid(firebaseUid);
    }

    public UsuarioPerfil obtenerPorEmail(String email) {
        return usuarioPerfilRepository.findByEmail(email);
    }

    public List<UsuarioPerfil> obtenerPorRol(Integer rolId) {
        return usuarioPerfilRepository.findByRolId(rolId);
    }

    public List<UsuarioPerfil> obtenerPorCentroAcopio(Integer centroAcopioId) {
        return usuarioPerfilRepository.findByCentroAcopioId(centroAcopioId);
    }

    public List<UsuarioPerfil> obtenerPorComuna(Integer comunaId) {
        return usuarioPerfilRepository.findByComunaId(comunaId);
    }
}