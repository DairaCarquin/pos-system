package com.cibertec.pos_system.service;

import com.cibertec.pos_system.dto.CajaVentaDTO;
import com.cibertec.pos_system.dto.CajaVentaDetalleDTO;
import com.cibertec.pos_system.entity.*;
import com.cibertec.pos_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CajaVentaService {

    @Autowired
    private CajaVentaRepository cajaVentaRepository;
    @Autowired
    private CajaVentaDetalleRepository cajaVentaDetalleRepository;
    @Autowired
    private CajaSesionRepository cajaSesionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MedioPagoRepository medioPagoRepository;
    @Autowired
    private ProductoRepository productoRepository;

    // CRUD básico para el controlador
    public List<CajaVentaEntity> listar() {
        return cajaVentaRepository.findAll();
    }

    public CajaVentaEntity crear(CajaVentaEntity venta) {
        return cajaVentaRepository.save(venta);
    }

    public CajaVentaEntity obtenerPorId(Long id) {
        return cajaVentaRepository.findById(id).orElse(null);
    }

    // --- AGREGADO: calcular total en caja por sesión (solo ventas finalizadas) ---
    public double calcularTotalEnCaja(Long cajaSesionId) {
        List<CajaVentaEntity> ventas = cajaVentaRepository.findAll();
        return ventas.stream()
                .filter(v -> v.getCajaSesion() != null
                        && v.getCajaSesion().getId().equals(cajaSesionId)
                        && "FINALIZADA".equals(v.getEstado()))
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal().doubleValue() : 0.0)
                .sum();
    }

    // --- NUEVO: calcular total en caja incluyendo monto inicial ---
    public BigDecimal calcularTotalEnCajaIncluyendoInicial(Long cajaSesionId) {
    CajaSesionEntity sesion = cajaSesionRepository.findById(cajaSesionId).orElse(null);
    if (sesion == null) return BigDecimal.ZERO;
    BigDecimal montoInicial = sesion.getMontoInicial() != null ? BigDecimal.valueOf(sesion.getMontoInicial()) : BigDecimal.ZERO;

    // Sumar solo ventas en efectivo y finalizadas
    BigDecimal ventasEfectivo = BigDecimal.ZERO;
    List<CajaVentaEntity> ventas = cajaVentaRepository.findAll();
    for (CajaVentaEntity v : ventas) {
        if (v.getCajaSesion() != null
                && v.getCajaSesion().getId().equals(cajaSesionId)
                && "FINALIZADA".equals(v.getEstado())
                && v.getMedioPago() != null
                && "EFECTIVO".equalsIgnoreCase(v.getMedioPago().getNombre())) {
            ventasEfectivo = ventasEfectivo.add(v.getTotal() != null ? v.getTotal() : BigDecimal.ZERO);
        }
    }
     System.out.println("Ventas efectivo: " + ventasEfectivo);
    System.out.println("Total sistema: " + montoInicial.add(ventasEfectivo));
    return montoInicial.add(ventasEfectivo);
}

    // Lógica de negocio para registrar venta con DTO (sin validación/descuento de stock)
    @Transactional
    public Long registrarVenta(CajaVentaDTO dto, String username) {
        System.out.println("Detalles recibidos: " + dto.getDetalles().size());
        // Validar sesión de caja
        CajaSesionEntity sesion = cajaSesionRepository.findById(dto.getCajaSesionId())
                .orElseThrow(() -> new RuntimeException("Sesión de caja no encontrada"));
        
        // Validar usuario/cajero
        UsuarioEntity cajero = usuarioRepository.findByUsername(username);
        if (cajero == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Validar cliente (opcional)
        ClienteEntity cliente = null;
        if (dto.getClienteId() != null) {
            cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        }

        // Validar medio de pago
        MedioPagoEntity medioPago = medioPagoRepository.findById(dto.getMedioPagoId())
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        // === Generar número de comprobante secuencial con prefijo de local ===
        String tipoComprobante = dto.getTipoComprobante();
        Long localId = sesion.getCaja().getLocal().getId();
        String[] palabras = sesion.getCaja().getLocal().getNombre().split("\\s+");
        String prefijo = palabras.length >= 3 ? palabras[2].substring(0, 2).toUpperCase() : "XX";
        List<String> numeros = cajaVentaRepository.findUltimoNumeroComprobantePorLocal(tipoComprobante, localId);
        String ultimo = null;
        for (String num : numeros) {
            if (num != null) {
                ultimo = num;
                break;
            }
        }
        String nuevoNumero;
        if (ultimo != null) {
            String ultimoNum = "000000";
            if (ultimo.length() > 2) {
                ultimoNum = ultimo.substring(2); // Quita el prefijo de 2 letras
            }
            int secuencia;
            try {
                secuencia = Integer.parseInt(ultimoNum) + 1;
            } catch (NumberFormatException e) {
                secuencia = 1;
            }
            nuevoNumero = prefijo + String.format("%06d", secuencia);
        } else {
            nuevoNumero = prefijo + "000001";
        }

        // Crear venta
        CajaVentaEntity venta = new CajaVentaEntity();
        venta.setCajaSesion(sesion);
        venta.setUsuario(cajero);
        venta.setCliente(cliente);
        venta.setMedioPago(medioPago);
        venta.setTipoComprobante(tipoComprobante);
        venta.setNumeroComprobante(nuevoNumero); // Asigna el número generado
        venta.setSubtotal(dto.getSubtotal());
        venta.setImpuesto(dto.getImpuesto());
        venta.setTotal(dto.getTotal());
        venta.setEstado("FINALIZADA");
        venta.setFechaHora(LocalDateTime.now());

        // Crear detalles (sin validar ni descontar stock)
        List<CajaVentaDetalleEntity> detalles = new ArrayList<>();
        for (CajaVentaDetalleDTO det : dto.getDetalles()) {
            ProductoEntity producto = productoRepository.findById(det.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            CajaVentaDetalleEntity detalle = new CajaVentaDetalleEntity();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(det.getCantidad());
            detalle.setPrecioUnitario(det.getPrecioUnitario());
            detalle.setSubtotal(det.getSubtotal());
            detalles.add(detalle);
        }
        venta.setDetalles(detalles);

        // Asignar la venta a cada detalle antes de guardar (por seguridad)
        if (venta.getDetalles() != null) {
            for (CajaVentaDetalleEntity detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
            }
        }

        // Guardar venta y detalles (cascade)
        CajaVentaEntity ventaGuardada = cajaVentaRepository.save(venta);
        return ventaGuardada.getId();
    }

    @Transactional
    public void anularVenta(Long ventaId, String motivo, String username) {
        CajaVentaEntity venta = cajaVentaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        if (!"FINALIZADA".equals(venta.getEstado())) {
            throw new RuntimeException("Solo se pueden anular ventas finalizadas");
        }
        venta.setEstado("ANULADA");
        venta.setMotivoAnulacion(motivo);
        venta.setFechaActualizacion(LocalDateTime.now());
        UsuarioEntity usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        venta.setUsuarioActualizacion(usuario);

        // Devolver stock
        for (CajaVentaDetalleEntity det : venta.getDetalles()) {
            ProductoEntity producto = det.getProducto();
            producto.setStockActual(producto.getStockActual() + det.getCantidad());
            productoRepository.save(producto);
        }
        cajaVentaRepository.save(venta);
    }
}