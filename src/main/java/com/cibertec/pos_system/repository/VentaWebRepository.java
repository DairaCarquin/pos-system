package com.cibertec.pos_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.pos_system.entity.VentaWeb;

public interface VentaWebRepository extends JpaRepository<VentaWeb, Long> {
    Optional<VentaWeb> findTopByOrderByIdDesc();

}