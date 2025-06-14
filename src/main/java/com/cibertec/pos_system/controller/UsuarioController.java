package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.UsuarioEntity;
import com.cibertec.pos_system.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioEntity> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    public UsuarioEntity crear(@RequestBody UsuarioEntity usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public UsuarioEntity actualizar(@PathVariable Long id, @RequestBody UsuarioEntity usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @GetMapping("/{id}")
    public UsuarioEntity obtener(@PathVariable Long id) {
        return usuarioService.obtener(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
