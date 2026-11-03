package com.avrilhernandez.escuela.repository;

import com.avrilhernandez.escuela.model.ProfesorMateria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorMateriaRepository
        extends JpaRepository<ProfesorMateria, Long> {

    boolean existsByProfesor_IdAndMateria_Id(
            Long profesorId, Long materiaId
    );
}

