package com.cibertec.pos_system.repository;

import com.cibertec.pos_system.entity.ProductoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
}
