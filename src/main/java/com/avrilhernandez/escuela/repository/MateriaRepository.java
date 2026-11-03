package com.avrilhernandez.escuela.repository;

import com.avrilhernandez.escuela.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
}
