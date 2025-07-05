package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.CategoriaEntity;
import com.cibertec.pos_system.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaEntity> listar() {
        return categoriaRepository.findAll();
    }

    public Page<CategoriaEntity> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoriaRepository.findAll(pageable);
    }

    public CategoriaEntity crear(CategoriaEntity categoria) {
        return categoriaRepository.save(categoria);
    }

    public CategoriaEntity actualizar(Long id, CategoriaEntity categoria) {
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    public Optional<CategoriaEntity> obtener(Long id) {
        return categoriaRepository.findById(id);
    }
}
