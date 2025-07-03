package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.service.CategoriaService;
import com.cibertec.pos_system.service.ProductoService;
import com.cibertec.pos_system.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/producto")
public class ProductoWebController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    public ProductoWebController(ProductoService productoService, 
                               CategoriaService categoriaService,
                               ProveedorService proveedorService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "producto/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new ProductoEntity());
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaProveedores", proveedorService.listar());
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ProductoEntity producto) {
        productoService.crear(producto);
        return "redirect:/producto/lista";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ProductoEntity producto = productoService.buscarPorId(id);
        if (producto == null) {
            return "redirect:/producto/lista";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("listaCategorias", categoriaService.listar());
        model.addAttribute("listaProveedores", proveedorService.listar());
        return "producto/formulario";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute ProductoEntity producto) {
        productoService.actualizar(producto.getId(), producto);
        return "redirect:/producto/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/producto/lista";
    }

    @GetMapping
    public String redirectToLista() {
        return "redirect:/producto/lista";
    }
} 