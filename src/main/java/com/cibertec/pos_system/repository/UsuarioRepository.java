package com.cibertec.pos_system.repository;

import com.cibertec.pos_system.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {}
