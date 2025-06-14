package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.UsuarioEntity;
import com.cibertec.pos_system.repository.UsuarioRepository;
import com.cibertec.pos_system.service.impl.UsuarioServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UsuarioServiceInterface {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioEntity> obtenerTodas() {
        return usuarioRepository.findAll();
    }
    @Override
    public UsuarioEntity obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public UsuarioEntity crearUser(UsuarioEntity user) {
        return usuarioRepository.save(user);
    }

    @Override
    public UsuarioEntity actualizarUser(Long id, UsuarioEntity user) {
        UsuarioEntity userBBDD = usuarioRepository.findById(id).orElse(null);

        if(userBBDD != null){
            // Actualizar roles
            return usuarioRepository.save(userBBDD);
        }
        return null;
    }

    @Override
    public void eliminarUser(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public long contarUsers() {
        return usuarioRepository.count();
    }

    @Override
    public Optional<UsuarioEntity> obtener(Long id) {
        return usuarioRepository.findById(id);
    }
}
