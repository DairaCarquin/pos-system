package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.UsuarioEntity;
import com.cibertec.pos_system.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<UsuarioEntity> listar() {
        return usuarioRepo.findAll();
    }

    public UsuarioEntity guardar(UsuarioEntity usuario) {
        return usuarioRepo.save(usuario);
    }

    public UsuarioEntity actualizar(Long id, UsuarioEntity usuario) {
        usuario.setId(id);
        return usuarioRepo.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepo.deleteById(id);
    }

    public Optional<UsuarioEntity> obtener(Long id) {
        return usuarioRepo.findById(id);
    }
}
