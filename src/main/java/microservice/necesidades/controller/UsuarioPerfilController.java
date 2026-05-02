package microservice.necesidades.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import microservice.necesidades.model.UsuarioPerfil;
import microservice.necesidades.service.UsuarioPerfilService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPerfilController {

    @Autowired
    private UsuarioPerfilService service;

    @GetMapping
    public ResponseEntity<List<UsuarioPerfil>> obtenerTodos() {
        List<UsuarioPerfil> lista = service.obtenerUsuarios();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPerfil> obtenerPorId(@PathVariable Integer id) {
        UsuarioPerfil usuario = service.obtenerUsuarioPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioPerfil> crear(@RequestBody UsuarioPerfil usuario) {
        return ResponseEntity.status(201).body(service.guardarUsuario(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioPerfil> actualizar(@PathVariable Integer id, @RequestBody UsuarioPerfil usuario) {
        UsuarioPerfil actualizado = service.actualizarUsuario(id, usuario);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioPerfil> actualizarParcial(@PathVariable Integer id, @RequestBody UsuarioPerfil usuario) {
        UsuarioPerfil actualizado = service.actualizarUsuarioParcial(id, usuario);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/firebase/{uid}")
    public ResponseEntity<UsuarioPerfil> obtenerPorFirebaseUid(@PathVariable String uid) {
        UsuarioPerfil usuario = service.obtenerPorFirebaseUid(uid);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioPerfil> obtenerPorEmail(@PathVariable String email) {
        UsuarioPerfil usuario = service.obtenerPorEmail(email);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioPerfil>> obtenerPorRol(@PathVariable Integer rolId) {
        List<UsuarioPerfil> lista = service.obtenerPorRol(rolId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }
}