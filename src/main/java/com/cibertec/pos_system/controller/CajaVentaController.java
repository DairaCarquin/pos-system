package com.cibertec.pos_system.controller;

import com.cibertec.pos_system.dto.CajaVentaDTO;
import com.cibertec.pos_system.dto.CajaVentaDetalleDTO;
import com.cibertec.pos_system.entity.*;
import com.cibertec.pos_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/caja-venta")
public class CajaVentaController {

    @Autowired
    private CajaVentaService cajaVentaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private MedioPagoService medioPagoService;
    @Autowired
    private CajaSesionService cajaSesionService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarVentas(Model model) {
        List<CajaVentaEntity> ventas = cajaVentaService.listar();
        model.addAttribute("listaVentas", ventas);
        return "caja/caja-ventas-listar";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute CajaVentaDTO ventaDTO, RedirectAttributes redirectAttributes) {
        // Buscar entidades relacionadas y validar
        CajaSesionEntity cajaSesion = cajaSesionService.obtenerPorId(ventaDTO.getCajaSesionId());
        if (cajaSesion == null) throw new RuntimeException("Sesión de caja no encontrada");

        ClienteEntity cliente = ventaDTO.getClienteId() != null ? clienteService.obtener(ventaDTO.getClienteId()).orElse(null) : null;

        MedioPagoEntity medioPago = medioPagoService.obtener(ventaDTO.getMedioPagoId()).orElse(null);
        if (medioPago == null) throw new RuntimeException("Medio de pago no encontrado");

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UsuarioEntity usuario = usuarioService.obtenerPorUsername(username);
        if (usuario == null) throw new RuntimeException("Usuario no encontrado");

        // Crear la venta (tu lógica actual)
        CajaVentaEntity venta = new CajaVentaEntity();
        venta.setCajaSesion(cajaSesion);
        venta.setCliente(cliente);
        venta.setMedioPago(medioPago);
        venta.setUsuario(usuario);
        venta.setFechaHora(LocalDateTime.now());
        venta.setSubtotal(ventaDTO.getSubtotal());
        venta.setImpuesto(ventaDTO.getImpuesto());
        venta.setTotal(ventaDTO.getTotal());
        venta.setTipoComprobante(ventaDTO.getTipoComprobante());
        venta.setEstado("FINALIZADA");

        // Crear detalles
        List<CajaVentaDetalleEntity> detalles = new ArrayList<>();
        if (ventaDTO.getDetalles() != null) {
            for (CajaVentaDetalleDTO detDTO : ventaDTO.getDetalles()) {
                ProductoEntity producto = productoService.obtener(detDTO.getProductoId()).orElse(null);
                if (producto == null) throw new RuntimeException("Producto no encontrado");
                CajaVentaDetalleEntity detalle = new CajaVentaDetalleEntity();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(detDTO.getCantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalles.add(detalle);
            }
        }
        venta.setDetalles(detalles);

        // --- AGREGADO: también registrar la venta usando el método que genera el número de comprobante ---
        Long idVenta = cajaVentaService.registrarVenta(ventaDTO, username);

        redirectAttributes.addFlashAttribute("exito", "¡Venta registrada correctamente!");
        // Redirigir directamente al comprobante de la venta recién creada
        return "redirect:/caja-venta/detalle/" + idVenta;
    }

    @GetMapping("/anular/{id}")
    public String mostrarFormularioAnular(@PathVariable Long id, Model model) {
        CajaVentaEntity venta = cajaVentaService.obtenerPorId(id);
        model.addAttribute("venta", venta);
        model.addAttribute("accion", "/caja-venta/anular/" + id);
        return "caja/caja-ventas-anular";
    }

    @PostMapping("/anular/{id}")
    public String anularVenta(@PathVariable Long id, @RequestParam String motivo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        cajaVentaService.anularVenta(id, motivo, username);
        return "redirect:/caja-venta";
    }

    @GetMapping("/detalle/{id}")
    public String detalleVenta(@PathVariable Long id, Model model) {
        CajaVentaEntity venta = cajaVentaService.obtenerPorId(id);
        model.addAttribute("venta", venta);
        return "caja/caja-venta-detalle";
    }
}