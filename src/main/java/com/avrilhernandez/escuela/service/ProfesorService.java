package com.avrilhernandez.escuela.service;

import com.avrilhernandez.escuela.model.Profesor;
import java.util.List;
import java.util.Optional;

public interface ProfesorService {

    List<Profesor> listar();

    Optional<Profesor> buscarPorId(Long id);

    Profesor guardar(Profesor profesor);

    Optional<Profesor> actualizar(Long id, Profesor profesor);

    boolean eliminar(Long id);
}
