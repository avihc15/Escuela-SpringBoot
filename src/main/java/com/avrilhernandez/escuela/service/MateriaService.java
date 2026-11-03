package com.avrilhernandez.escuela.service;

import com.avrilhernandez.escuela.model.Materia;
import java.util.List;
import java.util.Optional;

public interface MateriaService {

    List<Materia> listar();

    Optional<Materia> buscarPorId(Long id);

    Materia guardar(Materia materia);

    Optional<Materia> actualizar(Long id, Materia materia);

    boolean eliminar(Long id);
}
