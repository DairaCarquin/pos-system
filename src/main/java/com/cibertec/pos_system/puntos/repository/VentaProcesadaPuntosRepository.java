package com.cibertec.pos_system.puntos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.pos_system.puntos.entity.VentaProcesadaPuntos;

@Repository
public interface VentaProcesadaPuntosRepository extends JpaRepository<VentaProcesadaPuntos, Long> {
}