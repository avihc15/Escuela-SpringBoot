package com.sarah.escuela.repository;

import com.sarah.escuela.model.ProfesorMateria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorMateriaRepository
        extends JpaRepository<ProfesorMateria, Long> {

    boolean existsByProfesor_IdAndMateria_Id(
            Long profesorId, Long materiaId
    );
}

