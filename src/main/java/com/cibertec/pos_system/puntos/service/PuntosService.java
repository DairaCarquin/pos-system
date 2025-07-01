package com.cibertec.pos_system.puntos.service;

import com.cibertec.pos_system.entity.CajaVentaEntity;
import com.cibertec.pos_system.entity.ClienteEntity;
import com.cibertec.pos_system.repository.ClienteRepository; // Usa el repo existente
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PuntosService {

    private final ClienteRepository clienteRepository;

    public int asignarPuntosParaVenta(CajaVentaEntity venta) {
        ClienteEntity cliente = venta.getCliente();
        if (cliente == null) return 0;

        int puntosGanados = calcularPuntos(venta.getTotal(), cliente.getTipo());

        if (puntosGanados > 0) {
            int puntosActuales = cliente.getPuntosAcumulados() != null ? cliente.getPuntosAcumulados() : 0;
            cliente.setPuntosAcumulados(puntosActuales + puntosGanados);
            clienteRepository.save(cliente);
        }
        
        return puntosGanados;
    }

    private int calcularPuntos(BigDecimal montoTotal, String tipoCliente) {
        if (montoTotal == null || tipoCliente == null) return 0;
        BigDecimal factor = tipoCliente.equalsIgnoreCase("VIP") ? new BigDecimal("5") : new BigDecimal("10");
        return montoTotal.divide(factor, 0, BigDecimal.ROUND_FLOOR).intValue();
    }
}