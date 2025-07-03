package com.cibertec.pos_system.controller.ventas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.pos_system.dto.ItemCarrito;
import com.cibertec.pos_system.entity.ProductoEntity;
import com.cibertec.pos_system.entity.VentaDetalleWeb;
import com.cibertec.pos_system.entity.VentaWeb;
import com.cibertec.pos_system.enums.MetodoPago;
import com.cibertec.pos_system.repository.VentaWebRepository;
import com.cibertec.pos_system.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping( "/ventas-online")
public class VentasOnlineController {

    @Autowired
    private ProductoService serviceProducto;

    @Autowired
    private VentaWebRepository ventaWebRepository;

    // 
    // 
    // 
    // 
    
    @GetMapping( path="/list")
    public String ventasOnline(Model model){

        List<VentaWeb> ventas = ventaWebRepository.findAll(); // obntemeos todas las ventas
        model.addAttribute("ventas", ventas); // mandamos las ventas a la vista 
        
        
        return "home-ventas/ventas-online"; // ventas-online.html
    }

    // 
    // 
    // 
    // 

    @GetMapping("/detalle/{id}")
    public String detalleVenta(@PathVariable Long id, Model model) {
        Optional<VentaWeb> ventaOptional = ventaWebRepository.findById(id); // obtenemos una venda por id
    
        if (ventaOptional.isEmpty()) { // si la venta esta vacia mandamos un not foubnd
            return "redirect:/ventas-online/list?error=notfound";
        }

        VentaWeb venta = ventaOptional.get(); // en caso no lo esté, obtenemos la venta del optional
        model.addAttribute("venta", venta); // y lo mandamos a la vista
    
        return "home-ventas/detalle-venta"; // vista : detalle-venta.html
    }

    // 
    // 
    // 
    // 

    @PostMapping("/anular/{id}")
    public String anularVenta(@PathVariable Long id) {
        VentaWeb venta = ventaWebRepository.findById(id).orElse(null); // obtenemos la venta con el id de parametro

        if (venta == null || "ANULADO".equalsIgnoreCase(venta.getEstado())) { // si la venta es anullada 
            return "redirect:/ventas-online/list"; // retorna la vista de ventas
        }

        // Cambiar estado
        venta.setEstado("ANULADO"); // en caso que no, anulamos la venta

        // Devolver stock
        for (VentaDetalleWeb detalle : venta.getDetalles()) { // recorremos los detalles de la venta
            ProductoEntity producto = detalle.getProducto(); // obtenemos el producto de cada venta
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad()); // reducimos el sotck del producto que vamos anular
            serviceProducto.crear(producto); // actualizamos el stock del producto
        }

        // Guardar cambios
        ventaWebRepository.save(venta); // guardarmos los cambios

        return "redirect:/ventas-online/list"; // vista : ventas-online.html
    }

    // 
    // 
    // 
    // 

    @PostMapping("/ventas-web/confirmar")
    public String ventaWeb(@RequestParam MetodoPago metodoPago,HttpSession session){

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito"); // carrito de la sesion
        
        if(carrito == null || carrito.isEmpty()){ // verificamos que no esté vacio o null , sino nos manda a la pagina de productos
            return "redirect:/home/productos";
        }

        // Obtener montos desde la sesión

        BigDecimal subtotal = (BigDecimal) session.getAttribute("redondearSubtotal"); // subtotal de la sesion
        BigDecimal igv = (BigDecimal) session.getAttribute("redondearIgv"); // igv de la sesion
        BigDecimal total = (BigDecimal) session.getAttribute("redondearTotal"); // total de la sesion

        // Crear la venta

        VentaWeb venta = new VentaWeb(); // instancia nueva de venta

        // seteamos los datos
        venta.setFecha(LocalDateTime.now());
        venta.setMetodoPago(metodoPago);
        venta.setSubtotal(subtotal);
        venta.setImpuestos(igv);
        venta.setTotal(total);
        venta.setEstado("FINALIZADO");

        // Generamos numero de comprobante secuencial

        Optional<VentaWeb> ultimaVentaOpt = ventaWebRepository.findTopByOrderByIdDesc();
        String nuevoNumeroComprobante = "WEB-000001";

        if(ultimaVentaOpt.isPresent()){
            String ultimoComprobante = ultimaVentaOpt.get().getNumeroComprobante();
            int ultimoNumero = Integer.parseInt(ultimoComprobante.replace("WEB-", ""));
            int nuevoNumero = ultimoNumero + 1;
            nuevoNumeroComprobante = String.format("WEB-%06d", nuevoNumero);
        }

        venta.setNumeroComprobante(nuevoNumeroComprobante);


        // Crear los detalles

        List<VentaDetalleWeb> detalles = new ArrayList<>(); // creamos una lista para los detalles

        for(ItemCarrito item : carrito){

            ProductoEntity producto = serviceProducto.obtener(item.getProductoId()).orElse(null); // obntemoes le producto con el id
            
            
            if(producto == null){ // tiene que existir
                continue;
            }
            
            if(producto.getStockActual() < item.getQuantity()){ // en caso no haya stock
                return "redirect:/home/carrito?error=stockinsuficiente";
                
            }


            // seteamos el stock actual - el stock que vieen de la compra en e lcarrito
            producto.setStockActual(producto.getStockActual() - item.getQuantity());
            serviceProducto.crear(producto);
            
            VentaDetalleWeb detalle = new VentaDetalleWeb();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getQuantity());
            detalle.setPrecioUnitario(item.getPrecio());
            detalle.setSubtotal(BigDecimal.valueOf(item.getSubtotal()));
            detalle.setVentaWeb(venta);

            detalles.add(detalle);
        }



        venta.setDetalles(detalles);

        ventaWebRepository.save(venta);
        // Limpiar sesión ( carrito, subtotal, igv y total )
        session.removeAttribute("carrito");
        session.removeAttribute("redondearSubtotal");
        session.removeAttribute("redondearIgv");
        session.removeAttribute("redondearTotal");

        

        return "home-ventas/venta-confirmada";
    }
}
