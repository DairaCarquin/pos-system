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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import jakarta.transaction.Transactional;

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
    public String anularVenta(@PathVariable Long id,@RequestParam String motivoAnulacion) {
        VentaWeb venta = ventaWebRepository.findById(id).orElse(null); // obtenemos la venta con el id de parametro

        if (venta == null || "ANULADO".equalsIgnoreCase(venta.getEstado())) { // si la venta es anullada 
            return "redirect:/ventas-online/list"; // retorna la vista de ventas
        }

        // Cambiar estado
        venta.setEstado("ANULADO"); // en caso que no, anulamos la venta
        venta.setMotivoAnulacion(motivoAnulacion);

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
        session.removeAttribute("carritoQuantity");

        

        return "home-ventas/venta-confirmada";
    }



    @GetMapping("/nuevo")
        public String mostrarFormularioVenta(Model model) {
        model.addAttribute("venta", new VentaWeb());
        model.addAttribute("productos", serviceProducto.listar());

        return "home-ventas/venta-form"; 
    }
    @PostMapping("/guardar")
public String guardarVenta(@ModelAttribute VentaWeb venta) {

    // Validar y calcular montos
    BigDecimal subtotal = BigDecimal.ZERO;

    List<VentaDetalleWeb> detallesFinales = new ArrayList<>();

    for (VentaDetalleWeb detalle : venta.getDetalles()) {
        ProductoEntity producto = serviceProducto
            .obtener(detalle.getProducto().getId())
            .orElse(null);

        if (producto == null) continue;

        int cantidad = detalle.getCantidad();
        if (producto.getStockActual() < cantidad) {
            // si no hay suficiente stock, redirige con error (puedes personalizar esto)
            return "redirect:/ventas-online/nuevo?error=stockinsuficiente";
        }

        // Actualizar stock
        producto.setStockActual(producto.getStockActual() - cantidad);
        serviceProducto.crear(producto); // actualiza el producto

        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotalDetalle = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        // Armar el detalle
        detalle.setProducto(producto);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotalDetalle);
        detalle.setVentaWeb(venta); // link bidireccional

        detallesFinales.add(detalle);
        subtotal = subtotal.add(subtotalDetalle);
    }

    BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
    BigDecimal total = subtotal.add(igv);

    // Datos de la venta
    venta.setFecha(LocalDateTime.now());
    venta.setSubtotal(subtotal);
    venta.setImpuestos(igv);
    venta.setTotal(total);
    venta.setEstado("PROCESADO");

    // Número comprobante
    Optional<VentaWeb> ultimaVenta = ventaWebRepository.findTopByOrderByIdDesc();
    String nuevoComprobante = "WEB-000001";
    if (ultimaVenta.isPresent()) {
        String ultimo = ultimaVenta.get().getNumeroComprobante();
        int numero = Integer.parseInt(ultimo.replace("WEB-", ""));
        nuevoComprobante = String.format("WEB-%06d", numero + 1);
    }
    venta.setNumeroComprobante(nuevoComprobante);

    venta.setDetalles(detallesFinales);
    ventaWebRepository.save(venta);

    return "redirect:/ventas-online/list";
}


@GetMapping("/editar/{id}")
public String editarVenta(@PathVariable Long id, Model model) {
    VentaWeb venta = ventaWebRepository.findById(id).orElseThrow();

    // Validar que ningún detalle esté en null ni que tenga campos importantes nulos
    List<VentaDetalleWeb> detallesValidos = venta.getDetalles().stream()
        .filter(det -> det != null && det.getProducto() != null && det.getPrecioUnitario() != null)
        .toList();

    model.addAttribute("venta", venta);
    model.addAttribute("detalles", detallesValidos);
    return "home-ventas/venta-editar";
}

@PostMapping("/eliminar/{id}")
public String eliminarVenta(@PathVariable Long id) {
    Optional<VentaWeb> optionalVenta = ventaWebRepository.findById(id);

    if (optionalVenta.isPresent()) {
        VentaWeb venta = optionalVenta.get();

        // Restaurar stock de cada producto
        for (VentaDetalleWeb detalle : venta.getDetalles()) {
            ProductoEntity producto = detalle.getProducto();
            int cantidadVendida = detalle.getCantidad();

            // Sumar la cantidad vendida de nuevo al stock
            producto.setStockActual(producto.getStockActual() + cantidadVendida);

            serviceProducto.crear(producto); // actualiza el producto
        }

        // Ahora sí, eliminar la venta
        ventaWebRepository.deleteById(id);
    }

    return "redirect:/ventas-online/list";
}

    @PostMapping("/actualizar")
    @Transactional
    public String actualizarVenta(@ModelAttribute("venta") VentaWeb ventaForm) {
    
        VentaWeb ventaOriginal = ventaWebRepository.findById(ventaForm.getId()).orElse(null);
        if (ventaOriginal == null) {
            return "redirect:/ventas-online/list";
        }
    
        // Restaurar stock de productos originales
        for (VentaDetalleWeb detalleAntiguo : ventaOriginal.getDetalles()) {
            ProductoEntity productoBD = serviceProducto.buscarPorId(detalleAntiguo.getProducto().getId());
            if (productoBD != null) {
                productoBD.setStockActual(productoBD.getStockActual() + detalleAntiguo.getCantidad());
                serviceProducto.crear(productoBD);
            }
        }
    
        // Eliminar detalles antiguos
        ventaWebRepository.eliminarDetallesPorVentaId(ventaOriginal.getId());
        ventaOriginal.getDetalles().clear();
    
        // Agregar nuevos detalles
        for (VentaDetalleWeb nuevoDetalle : ventaForm.getDetalles()) {
            nuevoDetalle.setVentaWeb(ventaOriginal);
    
            BigDecimal subtotal = nuevoDetalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(nuevoDetalle.getCantidad()));
            nuevoDetalle.setSubtotal(subtotal);
    
            ProductoEntity productoBD = serviceProducto.buscarPorId(nuevoDetalle.getProducto().getId());
            if (productoBD != null) {
                productoBD.setStockActual(productoBD.getStockActual() - nuevoDetalle.getCantidad());
                serviceProducto.crear(productoBD);
            }
    
            ventaOriginal.getDetalles().add(nuevoDetalle);
        }
    
        // Calcular totales
        BigDecimal nuevoSubtotal = ventaOriginal.getDetalles().stream()
                .map(VentaDetalleWeb::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        BigDecimal igv = nuevoSubtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = nuevoSubtotal.add(igv);
    
        ventaOriginal.setSubtotal(nuevoSubtotal);
        ventaOriginal.setImpuestos(igv);
        ventaOriginal.setTotal(total);
    
        ventaWebRepository.save(ventaOriginal);
    
        return "redirect:/ventas-online/list";
    }
    



}
