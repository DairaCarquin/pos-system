package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.service.CompraService;
import com.cibertec.pos_system.entity.CompraEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class    HistorialCompraController {

    private final CompraService compraService;

    public HistorialCompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // Buscar historial por DNI (ruta clásica)
    @GetMapping("/cliente/{dni}")
    public String verHistorial(@PathVariable("dni") String dni, Model model) {
        List<CompraEntity> compras = compraService.listarComprasPorCliente(dni);
        model.addAttribute("cliente", compras);

        if (compras.size() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }

        return "cliente/historial";
    }

    // Buscar historial por ID (ruta clásica)
    @GetMapping("/cliente-id/{id}")
    public String verHistorialPorId(@PathVariable("id") Long clienteId, Model model) {
        List<CompraEntity> compras = compraService.listarComprasPorClienteId(clienteId);
        model.addAttribute("compras", compras);
        return "cliente/historial";
    }

    // ✅ Nueva ruta que detecta si es DNI o ID
    @GetMapping("/buscar")
    public String buscarCliente(@RequestParam("valor") String valor, Model model) {
        List<CompraEntity> compras;

        try {
            // Intentamos convertir a número, si funciona es un ID
            Long clienteId = Long.parseLong(valor);
            compras = compraService.listarComprasPorClienteId(clienteId);
        } catch (NumberFormatException e) {
            // Si falla, lo tratamos como DNI
            compras = compraService.listarComprasPorCliente(valor);
        }

        model.addAttribute("compras", compras);

        if (compras.size() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }

        return "cliente/historial";
    }
}
