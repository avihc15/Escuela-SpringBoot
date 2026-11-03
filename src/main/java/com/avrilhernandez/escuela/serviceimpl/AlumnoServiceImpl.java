package com.avrilhernandez.escuela.serviceimpl;

import com.avrilhernandez.escuela.model.Alumno;
import com.avrilhernandez.escuela.repository.AlumnoRepository;
import com.avrilhernandez.escuela.service.AlumnoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository repository;

    public AlumnoServiceImpl(AlumnoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Alumno> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Alumno> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Alumno guardar(Alumno alumno) {
        alumno.setId(null);
        return repository.save(alumno);
    }

    @Override
    public Optional<Alumno> actualizar(Long id, Alumno alumno) {
        return repository.findById(id).map(actual -> {
            actual.setNombre(alumno.getNombre());
            actual.setEdad(alumno.getEdad());
            actual.setPromedio(alumno.getPromedio());
            return repository.save(actual);
        });
    }

    @Override
    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}