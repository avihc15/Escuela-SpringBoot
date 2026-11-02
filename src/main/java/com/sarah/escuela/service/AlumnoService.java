package com.sarah.escuela.service;

import com.sarah.escuela.model.Alumno;
import java.util.List;
import java.util.Optional;

public interface AlumnoService {

    List<Alumno> listar();

    Optional<Alumno> buscarPorId(Long id);

    Alumno guardar(Alumno alumno);

    Optional<Alumno> actualizar(Long id, Alumno alumno);

    boolean eliminar(Long id);
}
