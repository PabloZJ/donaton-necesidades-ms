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
import microservice.necesidades.model.RolUsuario;
import microservice.necesidades.service.RolUsuarioService;

@RestController
@RequestMapping("/api/roles")
public class RolUsuarioController {

    @Autowired
    private RolUsuarioService service;

    @GetMapping
    public ResponseEntity<List<RolUsuario>> obtenerTodos() {
        List<RolUsuario> lista = service.obtenerRoles();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolUsuario> obtenerPorId(@PathVariable Integer id) {
        RolUsuario rol = service.obtenerRolPorId(id);
        if (rol == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    public ResponseEntity<RolUsuario> crear(@RequestBody RolUsuario rol) {
        return ResponseEntity.status(201).body(service.guardarRol(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolUsuario> actualizar(@PathVariable Integer id, @RequestBody RolUsuario rol) {
        RolUsuario actualizado = service.actualizarRol(id, rol);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RolUsuario> actualizarParcial(@PathVariable Integer id, @RequestBody RolUsuario rol) {
        RolUsuario actualizado = service.actualizarRolParcial(id, rol);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolUsuario> obtenerPorNombre(@PathVariable String nombre) {
        RolUsuario rol = service.obtenerPorNombre(nombre);
        if (rol == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rol);
    }
}