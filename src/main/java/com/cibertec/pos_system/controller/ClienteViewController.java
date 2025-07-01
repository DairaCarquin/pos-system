package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClienteViewController {

    private final ClienteService clienteService;

  @GetMapping("/panel-control")
    public String mostrarPanelDeControl() {
    // Apuntará al nuevo nombre del archivo HTML
    return "panel-control"; 
}

    @GetMapping("/cliente")
    public String listarClientes(Model model) {
        List<ClienteEntity> clientes = clienteService.listar();
        model.addAttribute("clientes", clientes);
        return "cliente/lista";
    }

    @GetMapping("/cliente/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {
        ClienteEntity cliente = clienteService.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        model.addAttribute("cliente", cliente);
        return "cliente/perfil";
    }
}