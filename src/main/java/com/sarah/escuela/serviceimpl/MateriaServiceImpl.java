package com.sarah.escuela.serviceimpl;

import com.sarah.escuela.model.Materia;
import com.sarah.escuela.repository.MateriaRepository;
import com.sarah.escuela.service.MateriaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository repository;

    public MateriaServiceImpl(MateriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Materia> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<Materia> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Materia guardar(Materia materia) {
        materia.setId(null);
        return repository.save(materia);
    }

    @Override
    public Optional<Materia> actualizar(Long id, Materia materia) {
        return repository.findById(id).map(actual -> {
            actual.setNombre(materia.getNombre());
            actual.setCreditos(materia.getCreditos());
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
