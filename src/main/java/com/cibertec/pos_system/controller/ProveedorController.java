package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.CategoriaEntity;
import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.entity.ProveedorEntity;
import com.cibertec.pos_system.service.CategoriaService;
import com.cibertec.pos_system.service.ProductoService;
import com.cibertec.pos_system.service.ProveedorService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vista/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProveedorController(ProveedorService proveedorService,
                               ProductoService productoService,
                               CategoriaService categoriaService) {
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        return "proveedor/proveedor_crear";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute("proveedor") ProveedorEntity proveedor, Model model) {
        if (proveedorService.buscarRazonSocial(proveedor.getRuc(), proveedor.getRazonSocial())) {
            model.addAttribute("error", "Ya existe un proveedor con el mismo RUC y razón social.");
            return "proveedor/proveedor_crear";
        }

        if (proveedorService.buscarRuc(proveedor.getRuc())) {
            model.addAttribute("error", "Ya existe un proveedor con el mismo RUC.");
            return "proveedor/proveedor_crear";
        }

        proveedor.setActivo(true);
        proveedorService.guardar(proveedor);
        return "redirect:/vista/proveedores/lista";
    }

    @GetMapping("/{proveedorId}/productos")
    public String mostrarFormularioAsociarProductos(@PathVariable Long proveedorId, Model model) {
        ProveedorEntity proveedor = proveedorService.obtener(proveedorId)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        ProductoEntity producto = new ProductoEntity();
        producto.setProveedor(proveedor);

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listar()); // Asegúrate que este método existe

        return "proveedor/proveedor_asociar_productos";
    }

    @PostMapping("/{proveedorId}/productos/guardar")
    public String guardarProducto(@PathVariable Long proveedorId, @ModelAttribute ProductoEntity producto) {
        ProveedorEntity proveedor = proveedorService.obtener(proveedorId)
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Long categoriaId = producto.getCategoria().getId();
        CategoriaEntity categoria = categoriaService.obtener(categoriaId)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        producto.setProveedor(proveedor);
        producto.setCategoria(categoria);
        producto.setActivo(true);
        productoService.crear(producto);

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

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable("id") Long id) {
        proveedorService.eliminar(id);
        return "redirect:/vista/proveedores/lista";
    }

    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id) {
        proveedorService.cambiarEstado(id, false);
        return "redirect:/vista/proveedores/lista";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute ProveedorEntity proveedor, Model model) {
        Optional<ProveedorEntity> proveedorExistente = proveedorService.obtener(proveedor.getId());
        if (proveedorExistente.isPresent()) {
            ProveedorEntity existente = proveedorExistente.get();
            if (!existente.getRuc().equals(proveedor.getRuc()) &&
                proveedorService.buscarRuc(proveedor.getRuc())) {
                model.addAttribute("error", "Ya existe otro proveedor con ese RUC.");
                model.addAttribute("proveedor", proveedor);
                return "proveedor/proveedor_editar";
            }
        }

        proveedorService.actualizar(proveedor.getId(), proveedor);
        return "redirect:/vista/proveedores/lista";
    }

    @GetMapping("/simulador")
    public String verSimulador() {
        return "simulador_proveedor"; // sin .html, porque Thymeleaf lo resuelve
    }

}
