package com.sarah.escuela.controller;

import com.sarah.escuela.model.Alumno;
import com.sarah.escuela.service.AlumnoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Alumno> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Alumno> guardar(@RequestBody Alumno alumno) {
        return ResponseEntity.ok(service.guardar(alumno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizar(
            @PathVariable Long id,
            @RequestBody Alumno alumno) {

        return service.actualizar(id, alumno)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}