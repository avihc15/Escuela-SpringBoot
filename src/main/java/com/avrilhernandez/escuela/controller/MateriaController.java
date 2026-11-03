package com.avrilhernandez.escuela.controller;

import com.avrilhernandez.escuela.model.Materia;
import com.avrilhernandez.escuela.service.MateriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService service;

    public MateriaController(MateriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Materia> obtenerMaterias() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Materia> guardar(@RequestBody Materia materia) {
        return ResponseEntity.ok(service.guardar(materia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> actualizar(
            @PathVariable Long id,
            @RequestBody Materia materia) {

        return service.actualizar(id, materia)
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
