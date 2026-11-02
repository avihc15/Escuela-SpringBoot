package com.sarah.escuela.repository;

import com.sarah.escuela.model.AlumnoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoMateriaRepository
        extends JpaRepository<AlumnoMateria, Long> {

    boolean existsByAlumno_IdAndMateria_Id(
            Long alumnoId, Long materiaId
    );
}
