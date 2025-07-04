package com.cibertec.pos_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cibertec.pos_system.entity.VentaWeb;

import jakarta.transaction.Transactional;

public interface VentaWebRepository extends JpaRepository<VentaWeb, Long> {
    Optional<VentaWeb> findTopByOrderByIdDesc();
    @Modifying
@Transactional
@Query("DELETE FROM VentaDetalleWeb d WHERE d.ventaWeb.id = :ventaId")
void eliminarDetallesPorVentaId(@Param("ventaId") Long ventaId);

}