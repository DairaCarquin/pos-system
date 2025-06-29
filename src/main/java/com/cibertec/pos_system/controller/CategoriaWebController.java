package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categoria")
public class CategoriaWebController {

    private final ProductoService productoService;

    public CategoriaWebController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", productoService.listarCategorias());
        return "categoria/lista";
    }
}
