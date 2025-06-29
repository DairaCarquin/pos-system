package com.cibertec.pos_system.controller;

<<<<<<< HEAD
import com.cibertec.pos_system.entity.ProductoEntity;
=======
>>>>>>> 1a21da03bd9c5eaadd6bf41db24cfd767702446c
import com.cibertec.pos_system.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 1a21da03bd9c5eaadd6bf41db24cfd767702446c
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
<<<<<<< HEAD
        return "producto/lista";
    }

    // ✅ Nuevo método para mostrar productos con descuento
    @GetMapping("/descuentos")
    public String listarConDescuento(Model model) {
        List<ProductoEntity> productosConDescuento = productoService.listarSoloConDescuento();
        model.addAttribute("productos", productosConDescuento);
        return "descuento/preciosConDescuento"; // tu vista ya creada
=======
        return "producto/lista"; // Asegúrate de tener templates/producto/lista.html
>>>>>>> 1a21da03bd9c5eaadd6bf41db24cfd767702446c
    }
}
