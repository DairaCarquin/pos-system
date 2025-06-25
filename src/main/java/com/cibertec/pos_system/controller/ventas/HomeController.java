package com.cibertec.pos_system.controller.ventas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.entity.ventas.VentaEntity;
import com.cibertec.pos_system.model.ventas.ItemCarrito;
import com.cibertec.pos_system.repository.ventas.VentaRepository;
import com.cibertec.pos_system.service.ProductoService;

import jakarta.servlet.http.HttpSession;

// java utils

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productService;
    @Autowired
    private VentaRepository ventaRepository;
    @GetMapping(path = "/")
    public String Page(HttpSession sesion,Model view){

        List<ItemCarrito> carrito = (List<ItemCarrito>) sesion.getAttribute("carrito");

        if(carrito != null){

            int quantity = carrito.size();
            view.addAttribute("carritoQuantity", quantity);
        }

        return "web/index";
    }


    // Todos los productos
    @GetMapping(path = "/productos")
    public String Productos(Model view,HttpSession sesion){

        List<ItemCarrito> carrito = (List<ItemCarrito>) sesion.getAttribute("carrito");
        List<ProductoEntity> productList = productService.listar();

        if(carrito != null){

            int quantity = carrito.size();
            view.addAttribute("carritoQuantity", quantity);
        }
        view.addAttribute("productlist", productList);
        return "web/productos";
    }

    // productos unicos

    @GetMapping( path = "/productos/{id}")
    public String ProductoID(@PathVariable Long id,Model model){


        Optional<ProductoEntity> prod = productService.obtener(id);

        
        model.addAttribute("productoid", prod.get());
        return "web/ProductosID";
    }

    @GetMapping( path = "about")
    public String AboutScreen(){
        return "web/about";
    }
    @GetMapping("/ventas")
public String listarVentas(Model model) {
    List<VentaEntity> ventas = ventaRepository.findAll();
    model.addAttribute("ventas", ventas);
    return "web/ventas";
}


}
