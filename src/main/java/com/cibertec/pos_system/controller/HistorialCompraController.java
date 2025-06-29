package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.entity.CompraEntity;
import com.cibertec.pos_system.repository.ClienteRepository;
import com.cibertec.pos_system.service.CompraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class HistorialCompraController {

    private final CompraService compraService;
    private final ClienteRepository clienteRepository; // ✅ agregado

    public HistorialCompraController(CompraService compraService, ClienteRepository clienteRepository) {
        this.compraService = compraService;
        this.clienteRepository = clienteRepository; // ✅ agregado
    }

    // 🔍 Buscar historial por DNI con paginación
    @GetMapping("/cliente/{dni}")
    public String verHistorial(
            @PathVariable("dni") String dni,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10); // ← puedes ajustar el tamaño de página
        Page<CompraEntity> comprasPage = compraService.listarComprasPorClientePaginado(dni, pageable);

        model.addAttribute("compras", comprasPage.getContent());
        model.addAttribute("totalPages", comprasPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("dni", dni);

        if (!comprasPage.isEmpty()) {
            model.addAttribute("nombre", comprasPage.getContent().get(0).getCliente().getNombre() + " " +
                    comprasPage.getContent().get(0).getCliente().getApellido());
        }

        if (comprasPage.getTotalElements() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }

        return "cliente/historial";
    }




    // 🔍 Buscar historial por ID sin paginación
    @GetMapping("/cliente-id/{id}")
    public String verHistorialPorId(@PathVariable("id") Long clienteId, Model model) {
        List<CompraEntity> compras = compraService.listarComprasPorClienteId(clienteId);
        model.addAttribute("compras", compras);

        if (!compras.isEmpty()) {
            model.addAttribute("dni", compras.get(0).getCliente().getDni());
            model.addAttribute("nombre", compras.get(0).getCliente().getNombre() + " " + compras.get(0).getCliente().getApellido());
        }

        if (compras.size() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }

        return "cliente/historial";
    }

    // 🔠 Listar clientes con filtro por letra e inicial + paginación
    @GetMapping("/listar")
    public String listarClientes(@RequestParam(value = "letra", required = false) String letra,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, 10); // 10 clientes por página
        Page<ClienteEntity> clientes;

        if (letra != null && !letra.isEmpty()) {
            clientes = clienteRepository.findByNombreStartingWithIgnoreCase(letra, pageable);
            model.addAttribute("letraSeleccionada", letra);
        } else {
            clientes = clienteRepository.findAll(pageable);
        }

        model.addAttribute("clientes", clientes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientes.getTotalPages());

        return "cliente/listado"; // ← CORREGIDO
    }


    // 🔍 Buscar por ID o DNI
    @GetMapping("/buscar")
    public String buscarCliente(@RequestParam("valor") String valor, Model model) {
        List<CompraEntity> compras;

        try {
            Long clienteId = Long.parseLong(valor);
            compras = compraService.listarComprasPorClienteId(clienteId);

            if (!compras.isEmpty()) {
                model.addAttribute("dni", compras.get(0).getCliente().getDni());
                model.addAttribute("nombre", compras.get(0).getCliente().getNombre() + " " + compras.get(0).getCliente().getApellido());
            }

        } catch (NumberFormatException e) {
            compras = compraService.listarComprasPorCliente(valor);

            if (!compras.isEmpty()) {
                model.addAttribute("dni", valor);
                model.addAttribute("nombre", compras.get(0).getCliente().getNombre() + " " + compras.get(0).getCliente().getApellido());
            }
        }

        model.addAttribute("compras", compras);

        if (compras.size() >= 10) {
            model.addAttribute("descuentoMensaje", "¡Felicidades! Tienes un 60% de descuento en tu próxima compra.");
        }

        return "cliente/historial";
    }

    @GetMapping("/compras")
    public String listarTodasLasComprasPaginadas(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<CompraEntity> comprasPage = compraService.listarTodasLasComprasPaginadas(pageable);

        model.addAttribute("compras", comprasPage.getContent());
        model.addAttribute("totalPages", comprasPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "cliente/historial";
    }

}
