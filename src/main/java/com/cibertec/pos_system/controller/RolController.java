package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.RolEntity;
import com.cibertec.pos_system.service.RolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolEntity> listar() {
        return rolService.listar();
    }

    @PostMapping
    public RolEntity crear(@RequestBody RolEntity rol) {
        return rolService.guardar(rol);
    }

    @PutMapping("/{id}")
    public RolEntity actualizar(@PathVariable Long id, @RequestBody RolEntity rol) {
        return rolService.actualizar(id, rol);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
    }

    @GetMapping("/{id}")
    public RolEntity obtener(@PathVariable Long id) {
        return rolService.obtener(id).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }
}
