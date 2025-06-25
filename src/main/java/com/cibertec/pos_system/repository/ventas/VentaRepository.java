package com.cibertec.pos_system.repository.ventas;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.pos_system.entity.ventas.VentaEntity;

public interface VentaRepository extends JpaRepository<VentaEntity,Integer> {
    
}
