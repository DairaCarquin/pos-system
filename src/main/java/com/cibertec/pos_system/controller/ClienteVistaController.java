package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteVistaController {

    private final ClienteService clienteService;

    public ClienteVistaController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
        return "cliente/registro"; // Esto busca cliente.html en /resources/templates
    }

    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") ClienteEntity cliente,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "cliente/registro"; // vuelve al formulario con errores
        }
        clienteService.guardar(cliente);
        return "redirect:/clientes/listado";
    }

    @GetMapping("/listado")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listar());
        return "cliente/listado"; // formulario.html
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        ClienteEntity cliente = clienteService.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de cliente inválido: " + id));
        model.addAttribute("cliente", cliente);
        return "cliente/nuevo"; // reutilizamos el mismo formulario
    }
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado correctamente");
        return "redirect:/clientes/listado";

    }
    @GetMapping
    public String redireccionarListado() {
        return "redirect:/clientes/listado";
    }


}