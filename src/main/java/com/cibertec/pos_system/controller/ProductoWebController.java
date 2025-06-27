package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoWebController {

    private final ProductoService productoService;

    public ProductoWebController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "producto/lista";
    }

    // ✅ Nuevo método para mostrar productos con descuento
    @GetMapping("/descuentos")
    public String listarConDescuento(Model model) {
        List<ProductoEntity> productosConDescuento = productoService.listarSoloConDescuento();
        model.addAttribute("productos", productosConDescuento);
        return "descuento/preciosConDescuento"; // tu vista ya creada
    }
}
