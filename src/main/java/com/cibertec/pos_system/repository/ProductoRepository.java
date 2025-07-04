package com.cibertec.pos_system.repository;

import com.cibertec.pos_system.entity.ProductoEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);

    Page<ProductoEntity> findByProveedorId(Long proveedorId, Pageable pageable);
    List<ProductoEntity> findByCategoriaId(Long categoriaId);
}
