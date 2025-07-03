// package com.cibertec.pos_system.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;

// @Controller
// public class HomeController {
//     @GetMapping("/")
//     public String home() {
//         // Redirige a la página de usuarios. Cambia "/user" por la ruta que prefieras.
//         return "redirect:/user";
//     }
// }

package com.cibertec.pos_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    
    @Autowired
    private ProductoService serviceProducto;

    // Pagina inicio
    @GetMapping( path="")
    public String home(Model model, HttpSession session){
        Integer cantidad = (Integer) session.getAttribute("carritoQuantity"); // obtenemos la cantidad del carrito en sesion
        model.addAttribute("carritoQuantity", cantidad != null ? cantidad : 0); // mandamos la cantidad a la vista
        return "home-ventas/home"; // vista : home.html
    }

    // 
    // 
    // 
    // Pagina productos
    @GetMapping (path = "home/productos")
    public String homeProductos(Model model,HttpSession session){
        Integer cantidad = (Integer) session.getAttribute("carritoQuantity"); // obtenemos la cantidad del carrito en sesion
        model.addAttribute("carritoQuantity", cantidad != null ? cantidad : 0); // mandamos la cantidad a la vista

        List<ProductoEntity> productos = serviceProducto.listar(); // listamos todos los productos
        model.addAttribute("productos", productos); // mandamos lso productos a al vista
        return "home-ventas/productos"; // vista : productos.html
    }
    // 
    // 
    // 
    // Pagina producto {id}
    @GetMapping( path="home/productos/{id}")
    public String homeProductosId(@PathVariable Long id,Model model,HttpSession session){

        Integer cantidad = (Integer) session.getAttribute("carritoQuantity"); // obtenemos la cantidad del carrito en sesion
        model.addAttribute("carritoQuantity", cantidad != null ? cantidad : 0); // mandamos la cantidad a la vista

        Optional<ProductoEntity> producto = serviceProducto.obtener(id); // obtenemos producto del service

        if (producto.isPresent()) { // verificamos si esta presente
            model.addAttribute("producto", producto.get()); // en caso esta presente mandamos el producto a la vista
        } else {
            return "redirect:/home/productos"; // en caso no esté presente,nos manda a productos.html
        }

        return "home-ventas/productoId"; // retorna a productoid.html
    }


    // 
    // 
    // 
    // Pagina about
    @GetMapping ( path="home/about")
    public String homeAbout(HttpSession session,Model model){

        Integer cantidad = (Integer) session.getAttribute("carritoQuantity"); // obtenemos la cantidad del carrito en sesion
        model.addAttribute("carritoQuantity", cantidad != null ? cantidad : 0); // mandamos la cantidad a la vista

        return "home-ventas/about"; // vista about.html
    }
}
