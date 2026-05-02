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
import microservice.necesidades.model.Necesidad;
import microservice.necesidades.service.NecesidadService;

@RestController
@RequestMapping("/api/necesidades")
public class NecesidadController {

    @Autowired
    private NecesidadService service;

    @GetMapping
    public ResponseEntity<List<Necesidad>> obtenerTodas() {
        List<Necesidad> lista = service.obtenerNecesidades();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Necesidad> obtenerPorId(@PathVariable Integer id) {
        Necesidad necesidad = service.obtenerNecesidadPorId(id);
        if (necesidad == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(necesidad);
    }

    @PostMapping
    public ResponseEntity<Necesidad> crear(@RequestBody Necesidad necesidad) {
        return ResponseEntity.status(201).body(service.guardarNecesidad(necesidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Necesidad> actualizar(@PathVariable Integer id, @RequestBody Necesidad necesidad) {
        Necesidad actualizada = service.actualizarNecesidad(id, necesidad);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Necesidad> actualizarParcial(@PathVariable Integer id, @RequestBody Necesidad necesidad) {
        Necesidad actualizada = service.actualizarNecesidadParcial(id, necesidad);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarNecesidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<Necesidad>> porEstado(@PathVariable Integer estadoId) {
        List<Necesidad> lista = service.obtenerPorEstado(estadoId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/reportador/{uid}")
    public ResponseEntity<List<Necesidad>> porReportador(@PathVariable String uid) {
        List<Necesidad> lista = service.obtenerPorReportador(uid);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/recurso/{tipoId}")
    public ResponseEntity<List<Necesidad>> porTipoRecurso(@PathVariable Integer tipoId) {
        List<Necesidad> lista = service.obtenerPorTipoRecurso(tipoId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }
}