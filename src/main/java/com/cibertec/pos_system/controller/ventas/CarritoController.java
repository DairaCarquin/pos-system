package com.cibertec.pos_system.controller.ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.entity.ventas.DetalleVentaEntity;
import com.cibertec.pos_system.entity.ventas.VentaEntity;
import com.cibertec.pos_system.model.ventas.ItemCarrito;
import com.cibertec.pos_system.repository.ventas.VentaRepository;
import com.cibertec.pos_system.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarritoController {
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaRepository ventaRepository;


    @PostMapping("/carrito/agregar")
    public String agregarCarrito(@RequestParam Long id, HttpSession session ) {
        Optional<ProductoEntity> isExit = productoService.obtener(id);

    if (isExit.isPresent()) {

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        // Ver si ya existe
        Optional<ItemCarrito> itemExistente = carrito.stream()
            .filter(item -> item.getProductoId().equals(isExit.get().getId()))
            .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setQuantity(item.getQuantity() + 1);
            item.setSubtotal(item.getPrecio() * item.getQuantity());
        } else {
            carrito.add(new ItemCarrito(
                isExit.get().getId(),
                isExit.get().getNombre(),
                isExit.get().getPrecio(),
                isExit.get().getPrecio(),
                1
            ));
        }

        double subtotal = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        double igv = subtotal * 0.18;
        double total = subtotal +igv;

        session.setAttribute("subtotal",subtotal);
        session.setAttribute("igv",igv);
        session.setAttribute("total",total);
        session.setAttribute("carrito", carrito);
    }

    return "redirect:/productos";
}



    @GetMapping(path = "/carrito")
    public String Carrito(HttpSession session,Model model){
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito"); // sesion -> obtenemos carrito

        if(carrito != null){

            double subtotal = (double) session.getAttribute("subtotal");
            double total = (double) session.getAttribute("total");
            double igv = (double) session.getAttribute("igv");
            
            
            model.addAttribute("carrito", carrito);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("total", total);
            model.addAttribute("igv", igv);
        }

        return "web/carrito";
    }




@PostMapping( "/carrito/finalizar")
public String finalizarCompra(@RequestParam String metodoPago, HttpSession session) {
    List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

    if (carrito == null || carrito.isEmpty()) {
        return "redirect:/carrito";
    }

    // 1. Calcular subtotal
    double subtotal = carrito.stream()
        .mapToDouble(ItemCarrito::getSubtotal)
        .sum();

    // 2. Calcular impuesto (18%)
    double impuesto = subtotal * 0.18;
    double total = subtotal + impuesto;

    // 3. Crear VentaEntity
    VentaEntity venta = new VentaEntity();
    venta.setFecha(LocalDate.now());
    venta.setSubtotal(subtotal);
    venta.setImpuesto(impuesto);
    venta.setTotal(total);

    // 4. Crear lista de detalles
    List<DetalleVentaEntity> detalles = new ArrayList<>();
    for (ItemCarrito item : carrito) {
        DetalleVentaEntity detalle = new DetalleVentaEntity();
        detalle.setNombreProducto(item.getNombre());
        detalle.setPrecioUnitario(item.getPrecio());
        detalle.setCantidad(item.getQuantity());
        detalle.setSubtotal(item.getSubtotal());
        detalle.setVenta(venta); // relación bidireccional
        detalles.add(detalle);
    }

    // 5. Asignar detalles a la venta
    venta.setDetalles(detalles);

    // 6. Guardar la venta (cascada guarda también los detalles)
    ventaRepository.save(venta);

    // 7. Reducir stock
    for (ItemCarrito item : carrito) {
        productoService.obtener(item.getProductoId()).ifPresent(producto -> {
            producto.setStock(producto.getStock() - item.getQuantity());
            productoService.crear(producto);
        });
    }

    // 8. Limpiar carrito
    session.removeAttribute("carrito");

    return "web/confirmacion";
}

}
