package com.cibertec.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.pos_system.entity.ClienteEntity;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByDni(String dni);
}