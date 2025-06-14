package com.cibertec.pos_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.pos_system.entity.CajaSesionEntity;

public interface CajaSesionRepository extends JpaRepository<CajaSesionEntity, Long> {
    List<CajaSesionEntity> findByCajaIdOrderByFechaAperturaDesc(Long cajaId);
    List<CajaSesionEntity> findByEstado(String estado);
}
