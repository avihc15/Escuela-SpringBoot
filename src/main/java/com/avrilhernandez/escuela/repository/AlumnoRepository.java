package com.avrilhernandez.escuela.repository;

import com.avrilhernandez.escuela.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
