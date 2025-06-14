package com.cibertec.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.pos_system.entity.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
