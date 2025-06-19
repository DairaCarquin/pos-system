package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.OrdenCompraDetalleEntity;
import com.cibertec.pos_system.entity.OrdenCompraEntity;
import com.cibertec.pos_system.service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/compras")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    @GetMapping("/ordenes")
    public String listar(Model model) {
        model.addAttribute("ordenes", ordenCompraService.listarOrdenes());
        return "orden/listado";
    }

    @GetMapping("/orden/nueva")
    public String nuevaOrden(Model model) {
        model.addAttribute("orden", new OrdenCompraEntity());
        return "orden/formulario";
    }

    @PostMapping("/orden")
    public String guardarOrden(@ModelAttribute OrdenCompraEntity orden, @RequestParam Long proveedorId, @RequestParam Long usuarioId, @ModelAttribute List<OrdenCompraDetalleEntity> detalles) {
        ordenCompraService.crearOrden(proveedorId, usuarioId, detalles);
        return "redirect:/compras/ordenes";
    }

    @GetMapping("/orden/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("orden", ordenCompraService.obtenerPorId(id));
        return "orden/detalle";
    }

    @PostMapping("/orden/{id}/recibir")
    public String marcarRecibida(@PathVariable Long id) {
        ordenCompraService.marcarComoRecibida(id);
        return "redirect:/compras/ordenes";
    }

    @PostMapping("/orden/{id}/cancelar")
    public String cancelarOrden(@PathVariable Long id) {
        ordenCompraService.cancelarOrden(id);
        return "redirect:/compras/ordenes";
    }
}
