package com.cibertec.pos_system.repository;

import com.cibertec.pos_system.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("SELECT u FROM UsuarioEntity u WHERE u.username = :username")
    UsuarioEntity getUserByUsername(@Param("username") String username);

    UsuarioEntity findByUsername(String username);

}