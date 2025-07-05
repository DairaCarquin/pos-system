package com.cibertec.pos_system.controller.ventas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.pos_system.dto.ItemCarrito;
import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
class CarritoController {
    
    @Autowired
    private ProductoService serviceProducto;


    @GetMapping( "/home/carrito")
    public String carrito(HttpSession session,Model model)
    {
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito"); // obtenemos la sesion de carrito al ingresar
        Integer cantidad = (Integer) session.getAttribute("carritoQuantity"); // obtenemos la cantidad del carrito en sesion
        model.addAttribute("carritoQuantity", cantidad != null ? cantidad : 0); // mandamos la cantidad a la vista

        BigDecimal subtotal = (BigDecimal) session.getAttribute("redondearSubtotal"); // obtenemos subtotal de la sesion
        BigDecimal igv = (BigDecimal) session.getAttribute("redondearIgv"); // igv de la sesion
        BigDecimal total = (BigDecimal) session.getAttribute("redondearTotal"); // total de la sesion

        model.addAttribute("carrito", carrito); // mandamos carrito a la vista
        model.addAttribute("subtotal", subtotal); // mandamos subtotal a la vista
        model.addAttribute("igv", igv); // mandamos igv a la vista
        model.addAttribute("total", total); // mandamos total a la vista
        return "home-ventas/carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarProductoAlCarrito(@RequestParam Long id,@RequestParam int cantidad, HttpSession session) {
    
        Optional<ProductoEntity> existeProducto = serviceProducto.obtener(id); // 1. Obtenemos el producto

        if (existeProducto.isPresent()) { // verificamos si producto no es null o vacio
            ProductoEntity productoPresente = existeProducto.get();  // si no es vacio ni nulo lo obtenemos

            if (productoPresente.getStockActual() <= 0) { // 2. Verificamos que haya stock disponible
                return "redirect:/home/productos"; // en caso no haya stock, regresamos a la zona de productos
            }

            @SuppressWarnings("unchecked")
            List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito"); // 3. Obtenemos la sesión del carrito

            if (carrito == null) { // 4. Si no hay carrito en la sesión
                carrito = new ArrayList<>(); // creamos una instancia del carrito
            }

            boolean existeDuplicado = false;

            for (ItemCarrito item : carrito) { // recorremo el carrito para ver si hay duplicados


                if (item.getProductoId().equals(productoPresente.getId())) { // si existe un producto con el mismo id del producto encontrado
                    // Ya existe, aumentamos cantidad y actualizamos subtotal
                    int nuevaCantidad = item.getQuantity() + cantidad; // la nueva cantidad será la cantidad que esta en el carrito + la cantidad que viene de parametro
                    item.setQuantity(nuevaCantidad); // actualizamos la nueva cantidad al item del carrito
                    item.setSubtotal(productoPresente.getPrecio().multiply(
                    java.math.BigDecimal.valueOf(nuevaCantidad)).doubleValue()); // calculamos de nuevo el subtotal ( quantity * precio )
                    existeDuplicado = true; // si existe un duplicado.
                    break;
            }
        }

        if (!existeDuplicado) { // en caso que no haya un duplicado simplemente agregamos un item al carrito
            carrito.add(
                new ItemCarrito(
                    productoPresente.getId(), 
                    productoPresente.getNombre(), 
                    productoPresente.getImagen(),
                    productoPresente.getPrecio(), 
                    productoPresente.getPrecio().doubleValue() * cantidad, 
                    cantidad)
            );
        }

        double subtotal =  carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum(); // calculamos el subtotal del carrito ( subtotal de todos los items se suma )
        double igv = subtotal * 0.18; // calculamos igv ( subtotal general * 0.18)
        double total = subtotal + igv; // finalmente calculamos el total ( subtotal + igv)

        BigDecimal redondearSubtotal = BigDecimal.valueOf(subtotal).setScale(2,RoundingMode.HALF_UP); // redondeamos subtotal a 2 decimales
        BigDecimal redondeadIgb = BigDecimal.valueOf(igv).setScale(2,RoundingMode.HALF_UP); // redondeamos igv a 2 decimales
        BigDecimal redondeadTotal = BigDecimal.valueOf(total).setScale(2,RoundingMode.HALF_UP); // redondeamos total a 2 decimales

        // Actualizamos carrito de la sesión
        session.setAttribute("carrito", carrito); // actualizamos carrito ( session )
        session.setAttribute("carritoQuantity", carrito.size()); // cantidad de carrio ( session )
        session.setAttribute("redondearSubtotal", redondearSubtotal); // actualizamos subtotal ( session )
        session.setAttribute("redondearIgv", redondeadIgb); // actualizamos igv ( session )
        session.setAttribute("redondearTotal", redondeadTotal); // actualizamos total ( session )
        
        }

        return "redirect:/home/productos/"+id; // redigimos al mismo lugar
    }



    @PostMapping("/carrito/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        
        @SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito"); // obtenemos carrito de la sesion

        if (carrito != null) { // verificamos si 
            carrito.removeIf(item -> item.getProductoId().equals(id)); // revemos si existe un producto con ese id

            // Recalcular totales
            double subtotal = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum(); // calculamos de nuevo el subtotal ( ya que eliminamos )
            double igv = subtotal * 0.18; // calculamos nuevamente el igv ( ya que eliminamos )
            double total = subtotal + igv; // calculamos total ( ya que eliminamos )

            BigDecimal redondearSubtotal = BigDecimal.valueOf(subtotal).setScale(2, RoundingMode.HALF_UP); // reondeamos a 2 decimales
            BigDecimal redondearIgv = BigDecimal.valueOf(igv).setScale(2, RoundingMode.HALF_UP); // redondeamos a 2 decimales
            BigDecimal redondearTotal = BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP); // redondeamos a 2 decimales

            session.setAttribute("carrito", carrito); // actualizamos carrito ( session )
            session.setAttribute("carritoQuantity", carrito.size()); // cantidad de carrio ( session )
            session.setAttribute("redondearSubtotal", redondearSubtotal); // actualizamos subtotal ( session )
            session.setAttribute("redondearIgv", redondearIgv); // actualizamos igv ( session )
            session.setAttribute("redondearTotal", redondearTotal); // actualizamos total ( session )
        }

        return "redirect:/home/carrito"; // al eliminar igual nos manda al mismo carrito
    }


}
