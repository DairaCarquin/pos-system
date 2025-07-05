package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.LocalEntity;
import com.cibertec.pos_system.repository.LocalRepository;
import com.cibertec.pos_system.service.impl.LocalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalService implements LocalServiceInterface {

    @Autowired
    private LocalRepository localRepository;

    @Override
    public List<LocalEntity> listar() {
        return localRepository.findAll();
    }

    public Page<LocalEntity> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return localRepository.findAll(pageable);
    }

    @Override
    public LocalEntity obtenerPorId(Long id) {
        return localRepository.findById(id).orElse(null);
    }

    @Override
    public LocalEntity crear(LocalEntity local) {
        return localRepository.save(local);
    }

    @Override
    public LocalEntity actualizar(Long id, LocalEntity local) {
        LocalEntity localBBDD = localRepository.findById(id).orElse(null);

        if (localBBDD != null) {
            // Actualiza los campos necesarios
            localBBDD.setNombre(local.getNombre());
            localBBDD.setDireccion(local.getDireccion());
            localBBDD.setTelefono(local.getTelefono());
            localBBDD.setEmail(local.getEmail());
            localBBDD.setHorario(local.getHorario());
            localBBDD.setActivo(local.isActivo());
            return localRepository.save(localBBDD);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        localRepository.deleteById(id);
    }

    @Override
    public long contar() {
        return localRepository.count();
    }

    @Override
    public Optional<LocalEntity> obtener(Long id) {
        return localRepository.findById(id);
    }
}