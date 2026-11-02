package com.sarah.escuela.controller;

import com.sarah.escuela.model.Profesor;
import com.sarah.escuela.service.ProfesorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    private final ProfesorService service;

    public ProfesorController(ProfesorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Profesor> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profesor> guardar(@RequestBody Profesor profesor) {
        return ResponseEntity.ok(service.guardar(profesor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizar(
            @PathVariable Long id,
            @RequestBody Profesor profesor) {

        return service.actualizar(id, profesor)
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
