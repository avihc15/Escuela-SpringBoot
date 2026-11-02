
package com.sarah.escuela.controller;

import com.sarah.escuela.service.AsignacionService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
public class AsignacionController {

    private final AsignacionService service;

    public AsignacionController(AsignacionService service) {
        this.service = service;
    }

    @GetMapping("/asignaciones")
    public String pagina() {
        return "asignaciones";
    }

    @GetMapping("/api/asignaciones/alumnos")
    @ResponseBody
    public List<Map<String, Object>> listarInscripciones() {
        return service.listarAlumnosMaterias();
    }

    @GetMapping("/api/asignaciones/profesores")
    @ResponseBody
    public List<Map<String, Object>> listarAsignaciones() {
        return service.listarProfesoresMaterias();
    }

    @PostMapping("/api/asignaciones/alumnos")
    public ResponseEntity<Void> inscribir(
            @RequestBody InscripcionRequest datos) {

        service.inscribirAlumno(
                datos.alumnoId(),
                datos.materiaId(),
                datos.calificacion()
        );

        return ResponseEntity.created(
                URI.create("/api/asignaciones/alumnos")
        ).build();
    }

    @PostMapping("/api/asignaciones/profesores")
    public ResponseEntity<Void> asignar(
            @RequestBody ProfesorRequest datos) {

        service.asignarProfesor(
                datos.profesorId(),
                datos.materiaId()
        );

        return ResponseEntity.created(
                URI.create("/api/asignaciones/profesores")
        ).build();
    }

    @PutMapping("/api/asignaciones/alumnos/{id}")
    public ResponseEntity<Void> calificar(
            @PathVariable Long id,
            @RequestBody CalificacionRequest datos) {

        service.actualizarCalificacion(id, datos.calificacion());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/asignaciones/alumnos/{id}")
    public ResponseEntity<Void> quitarInscripcion(
            @PathVariable Long id) {

        service.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/asignaciones/profesores/{id}")
    public ResponseEntity<Void> quitarAsignacion(
            @PathVariable Long id) {

        service.eliminarAsignacionProfesor(id);
        return ResponseEntity.noContent().build();
    }

    public record InscripcionRequest(
            Long alumnoId,
            Long materiaId,
            Double calificacion
    ) {}

    public record ProfesorRequest(
            Long profesorId,
            Long materiaId
    ) {}

    public record CalificacionRequest(Double calificacion) {}
}
