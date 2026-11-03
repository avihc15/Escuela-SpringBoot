package com.avrilhernandez.escuela.service;

import com.avrilhernandez.escuela.model.Alumno;
import java.util.List;
import java.util.Optional;

public interface AlumnoService {

    List<Alumno> listar();

    Optional<Alumno> buscarPorId(Long id);

    Alumno guardar(Alumno alumno);

    Optional<Alumno> actualizar(Long id, Alumno alumno);

    boolean eliminar(Long id);
}