package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.CajaEntity;
import com.cibertec.pos_system.service.CajaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cajas")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @GetMapping
    public List<CajaEntity> listar() {
        return cajaService.listar();
    }

    @PostMapping
    public CajaEntity crear(@RequestBody CajaEntity caja) {
        return cajaService.crear(caja);
    }

    @PutMapping("/{id}")
    public CajaEntity actualizar(@PathVariable Long id, @RequestBody CajaEntity caja) {
        return cajaService.actualizar(id, caja);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        cajaService.eliminar(id);
    }

    @GetMapping("/{id}")
    public CajaEntity obtener(@PathVariable Long id) {
        return cajaService.obtener(id)
                .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
    }
}
