
package com.sarah.escuela.service;

import java.util.List;
import java.util.Map;

public interface AsignacionService {

    List<Map<String, Object>> listarAlumnosMaterias();

    List<Map<String, Object>> listarProfesoresMaterias();

    void inscribirAlumno(
            Long alumnoId, Long materiaId, Double calificacion
    );

    void asignarProfesor(Long profesorId, Long materiaId);

    void actualizarCalificacion(Long id, Double calificacion);

    void eliminarInscripcion(Long id);

    void eliminarAsignacionProfesor(Long id);
}
