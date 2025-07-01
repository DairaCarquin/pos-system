package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.service.CajaVentaService;
import com.cibertec.pos_system.entity.CajaVentaEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class HistorialVentaController {

    private final CajaVentaService cajaVentaService;

    public HistorialVentaController(CajaVentaService cajaVentaService) {
        this.cajaVentaService = cajaVentaService;
    }

    // Buscar historial de ventas por DNI
    @GetMapping("/cliente/{dni}")
    public String verHistorial(@PathVariable("dni") String dni, Model model) {
        List<CajaVentaEntity> ventas = cajaVentaService.listarVentasPorClienteDni(dni);
        model.addAttribute("ventas", ventas);
        model.addAttribute("dni", dni);
        if (ventas.size() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }
        return "cliente/historial";
    }

    // Buscar historial de ventas por ID
    @GetMapping("/cliente-id/{id}")
    public String verHistorialPorId(@PathVariable("id") Long clienteId, Model model) {
        List<CajaVentaEntity> ventas = cajaVentaService.listarVentasPorClienteId(clienteId);
        model.addAttribute("ventas", ventas);
        model.addAttribute("clienteId", clienteId);
        return "cliente/historial";
    }

    // Nueva ruta que detecta si es DNI o ID
    @GetMapping("/buscar")
    public String buscarCliente(@RequestParam("valor") String valor, Model model) {
        List<CajaVentaEntity> ventas;
        try {
            Long clienteId = Long.parseLong(valor);
            ventas = cajaVentaService.listarVentasPorClienteId(clienteId);
            model.addAttribute("clienteId", clienteId);
        } catch (NumberFormatException e) {
            ventas = cajaVentaService.listarVentasPorClienteDni(valor);
            model.addAttribute("dni", valor);
        }
        model.addAttribute("ventas", ventas);
        if (ventas.size() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }
        return "cliente/historial";
    }
}
