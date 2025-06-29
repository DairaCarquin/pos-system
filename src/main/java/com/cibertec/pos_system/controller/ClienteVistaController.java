package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.repository.ClienteRepository;
import com.cibertec.pos_system.repository.CompraRepository;
import com.cibertec.pos_system.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteVistaController {

    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final CompraRepository compraRepository;

    public ClienteVistaController(ClienteService clienteService, CompraRepository compraRepository, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.compraRepository = compraRepository; // <-- y aquí
        this.clienteRepository = clienteRepository;
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
    public String listarClientes(
            @RequestParam(name = "filtro", required = false) String filtro,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "9") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClienteEntity> clientesPage;

        if (filtro != null && !filtro.trim().isEmpty()) {
            clientesPage = clienteService.buscarPorDni(filtro, pageable);

            if (clientesPage.hasContent()) {
                model.addAttribute("mensaje", "Cliente(s) encontrado(s) por DNI: " + filtro);
            } else {
                model.addAttribute("mensajeError", "No se encontró cliente con DNI: " + filtro);
            }

        } else {
            clientesPage = clienteService.listarPaginado(pageable);
        }

        // ✅ Asegurarse que no sea null antes de usar .getContent()
        model.addAttribute("clientes", clientesPage.getContent());
        model.addAttribute("currentPage", clientesPage.getNumber());
        model.addAttribute("totalPages", clientesPage.getTotalPages());
        model.addAttribute("filtro", filtro);

        return "cliente/listado";
    }






    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        ClienteEntity cliente = clienteService.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de cliente inválido: " + id));
        model.addAttribute("cliente", cliente);
        return "cliente/nuevo"; // reutilizamos el mismo formulario
    }
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id, RedirectAttributes redirect) {
        if (compraRepository.existsByClienteId(id)) {
            redirect.addFlashAttribute("mensajeError", "No se puede eliminar el cliente porque tiene historial de compras.");
        } else {
            clienteService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Cliente eliminado correctamente.");
        }
        return "redirect:/clientes/listado";
    }


    @GetMapping
    public String redireccionarListado() {
        return "redirect:/clientes/listado";
    }


}