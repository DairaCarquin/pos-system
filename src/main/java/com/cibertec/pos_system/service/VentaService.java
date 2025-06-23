package com.cibertec.pos_system.service;

import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.entity.VentaDetalleEntity;
import com.cibertec.pos_system.entity.VentaEntity;
import com.cibertec.pos_system.repository.ClienteRepository;
import com.cibertec.pos_system.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*Este servicio maneja lo siguiente: Registra, Calcula y Asigna puntos por compra al cliente segun su caso */

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public VentaEntity registrarVenta(VentaEntity venta) {
        // Establecer la fecha actual
        venta.setFecha(LocalDateTime.now());

        // Calcular el total de la venta
        BigDecimal total = calcularTotal(venta.getDetalles());
        venta.setTotal(total);

        // Guardar la venta
        VentaEntity ventaGuardada = ventaRepository.save(venta);

        // Asignar puntos si corresponde
        if (venta.isRecibePuntos()) {
            asignarPuntos(ventaGuardada.getCliente(), total);
        }

        return ventaGuardada;
    }

    private BigDecimal calcularTotal(List<VentaDetalleEntity> detalles) {
        return detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void asignarPuntos(ClienteEntity cliente, BigDecimal total) {
        int puntos = calcularPuntosPorTipoCliente(cliente.getTipo(), total);
        int puntosActuales = cliente.getPuntosAcumulados() != null ? cliente.getPuntosAcumulados() : 0;
        cliente.setPuntosAcumulados(puntosActuales + puntos);
        clienteRepository.save(cliente);
    }

    private int calcularPuntosPorTipoCliente(String tipo, BigDecimal total) {
        switch (tipo.toLowerCase()) {
            case "vip":
                return total.divide(BigDecimal.valueOf(5), 0, RoundingMode.DOWN).intValue();
        case "regular":
            default:
                return total.divide(BigDecimal.valueOf(10), 0, RoundingMode.DOWN).intValue();
        }
    }
}