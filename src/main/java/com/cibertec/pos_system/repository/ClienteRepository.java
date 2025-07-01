package com.cibertec.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.pos_system.entity.ClienteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Page<ClienteEntity> findByNombreStartingWithIgnoreCase(String letra, Pageable pageable);
    List<ClienteEntity> findByDniContainingIgnoreCase(String dni);


    List<ClienteEntity> findByDni(String dni);
    // Optional<ClienteEntity> findByDni(String dni);
    Page<ClienteEntity> findByDniContaining(String dni, Pageable pageable);


}
