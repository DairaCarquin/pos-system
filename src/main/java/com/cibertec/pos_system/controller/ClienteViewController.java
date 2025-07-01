package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cliente") 
@RequiredArgsConstructor
public class ClienteViewController {

    private final ClienteService clienteService;

    // Ver perfil de un cliente
    @GetMapping("/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {
        ClienteEntity cliente = clienteService.obtener(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        model.addAttribute("cliente", cliente);
        return "perfil/perfil";
    }

    // Lista de clientes
    @GetMapping
    public String listarClientes(Model model) {
        List<ClienteEntity> clientes = clienteService.listar();
        model.addAttribute("clientes", clientes);
        return "cliente/listar";
    }

    // Mostrar formulario para nuevo cliente
    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
        return "cliente/formulario";
    }

    // Mostrar formulario para editar un cliente
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        ClienteEntity cliente = clienteService.obtener(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        model.addAttribute("cliente", cliente);
        return "cliente/formulario";
    }

    // Guardar cliente (nuevo o editado)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") ClienteEntity cliente) {
        clienteService.guardar(cliente);
        return "redirect:/cliente";
    }

    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
        return "redirect:/cliente";
    }
}
