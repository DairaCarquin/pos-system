package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.CajaEntity;
import com.cibertec.pos_system.repository.CajaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    private final CajaRepository cajaRepository;

    public CajaService(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    public List<CajaEntity> listar() {
        return cajaRepository.findAll();
    }

    public CajaEntity crear(CajaEntity caja) {
        return cajaRepository.save(caja);
    }

    public CajaEntity actualizar(Long id, CajaEntity caja) {
        caja.setId(id);
        return cajaRepository.save(caja);
    }

    public void eliminar(Long id) {
        cajaRepository.deleteById(id);
    }

    public Optional<CajaEntity> obtener(Long id) {
        return cajaRepository.findById(id);
    }
}
