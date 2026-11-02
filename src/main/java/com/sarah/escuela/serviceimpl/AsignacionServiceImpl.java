
package com.sarah.escuela.serviceimpl;

import com.sarah.escuela.model.*;
import com.sarah.escuela.repository.*;
import com.sarah.escuela.service.AsignacionService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AsignacionServiceImpl implements AsignacionService {

    private final AlumnoRepository alumnos;
    private final ProfesorRepository profesores;
    private final MateriaRepository materias;
    private final AlumnoMateriaRepository inscripciones;
    private final ProfesorMateriaRepository asignaciones;

    public AsignacionServiceImpl(
            AlumnoRepository alumnos,
            ProfesorRepository profesores,
            MateriaRepository materias,
            AlumnoMateriaRepository inscripciones,
            ProfesorMateriaRepository asignaciones) {

        this.alumnos = alumnos;
        this.profesores = profesores;
        this.materias = materias;
        this.inscripciones = inscripciones;
        this.asignaciones = asignaciones;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarAlumnosMaterias() {
        return inscripciones.findAll().stream()
                .map(r -> {
                    Map<String, Object> fila =
                            new java.util.LinkedHashMap<>();
                    fila.put("id", r.getId());
                    fila.put("alumnoId", r.getAlumno().getId());
                    fila.put("alumno", r.getAlumno().getNombre());
                    fila.put("materiaId", r.getMateria().getId());
                    fila.put("materia", r.getMateria().getNombre());
                    fila.put("calificacion", r.getCalificacion());
                    return fila;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarProfesoresMaterias() {
        return asignaciones.findAll().stream()
                .map(r -> Map.<String, Object>of(
                        "id", r.getId(),
                        "profesorId", r.getProfesor().getId(),
                        "profesor", r.getProfesor().getNombre(),
                        "materiaId", r.getMateria().getId(),
                        "materia", r.getMateria().getNombre()
                ))
                .toList();
    }

    @Override
    public void inscribirAlumno(
            Long alumnoId, Long materiaId, Double calificacion) {

        validarCalificacion(calificacion);

        if (inscripciones.existsByAlumno_IdAndMateria_Id(
                alumnoId, materiaId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El alumno ya está inscrito en esta materia"
            );
        }

        Alumno alumno = alumnos.findById(alumnoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Alumno no encontrado"));

        Materia materia = materias.findById(materiaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Materia no encontrada"));

        AlumnoMateria registro = new AlumnoMateria();
        registro.setAlumno(alumno);
        registro.setMateria(materia);
        registro.setCalificacion(
                calificacion == null ? null : BigDecimal.valueOf(calificacion)
        );

        inscripciones.save(registro);
    }

    @Override
    public void asignarProfesor(Long profesorId, Long materiaId) {

        if (asignaciones.existsByProfesor_IdAndMateria_Id(
                profesorId, materiaId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El profesor ya tiene asignada esta materia"
            );
        }

        Profesor profesor = profesores.findById(profesorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profesor no encontrado"));

        Materia materia = materias.findById(materiaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Materia no encontrada"));

        ProfesorMateria registro = new ProfesorMateria();
        registro.setProfesor(profesor);
        registro.setMateria(materia);

        asignaciones.save(registro);
    }

    @Override
    public void actualizarCalificacion(
            Long id, Double calificacion) {

        validarCalificacion(calificacion);

        AlumnoMateria registro = inscripciones.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Inscripción no encontrada"));

        registro.setCalificacion(
                calificacion == null ? null : BigDecimal.valueOf(calificacion)
        );
        inscripciones.save(registro);
    }

    @Override
    public void eliminarInscripcion(Long id) {
        if (!inscripciones.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Inscripción no encontrada");
        }
        inscripciones.deleteById(id);
    }

    @Override
    public void eliminarAsignacionProfesor(Long id) {
        if (!asignaciones.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Asignación no encontrada");
        }
        asignaciones.deleteById(id);
    }

    private void validarCalificacion(Double calificacion) {
        if (calificacion != null &&
                (!Double.isFinite(calificacion) ||
                        calificacion < 0 || calificacion > 10)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La calificación debe estar entre 0 y 10"
            );
        }
    }
}

