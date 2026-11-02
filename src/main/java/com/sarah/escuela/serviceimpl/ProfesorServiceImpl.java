package com.sarah.escuela.serviceimpl;

import com.sarah.escuela.model.Profesor;
import com.sarah.escuela.repository.ProfesorRepository;
import com.sarah.escuela.service.ProfesorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository repository;

    public ProfesorServiceImpl(ProfesorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Profesor> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Profesor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Profesor guardar(Profesor profesor) {
        profesor.setId(null);
        return repository.save(profesor);
    }

    @Override
    public Optional<Profesor> actualizar(Long id, Profesor profesor) {
        return repository.findById(id).map(actual -> {
            actual.setNombre(profesor.getNombre());
            actual.setEspecialidad(profesor.getEspecialidad());
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