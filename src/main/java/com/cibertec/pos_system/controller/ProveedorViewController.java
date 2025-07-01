package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ProveedorEntity;
import com.cibertec.pos_system.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vista/proveedores")
public class ProveedorViewController {

    private final ProveedorService proveedorService;

    public ProveedorViewController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        return "proveedor/proveedor_crear";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute("proveedor") ProveedorEntity proveedor) {
        proveedor.setActivo(true);
        proveedorService.guardar(proveedor);
        return "redirect:/vista/proveedores/lista";
    }

    @GetMapping("/lista")
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.listar());
        return "proveedor/proveedor_lista";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        ProveedorEntity proveedor = proveedorService.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de proveedor inválido: " + id));
        model.addAttribute("proveedor", proveedor);
        return "proveedor/proveedor_editar";
    }

    @PostMapping("/actualizar")
    public String actualizarProveedor(@ModelAttribute("proveedor") ProveedorEntity proveedor) {
        proveedorService.actualizar(proveedor.getId(), proveedor);
        return "redirect:/vista/proveedores/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable("id") Long id) {
        proveedorService.eliminar(id);
        return "redirect:/vista/proveedores/lista";
    }
}
