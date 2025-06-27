package com.cibertec.pos_system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Data
public class CajaVentaDTO {
    private Long id; // opcional, para anulación o edición
    private Long cajaSesionId;
    private Long clienteId;
    private Long medioPagoId; // Debe ser Long para recibir el ID numérico del método de pago
    private String tipoComprobante;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal total;
    private List<CajaVentaDetalleDTO> detalles = new ArrayList<>(); // <-- inicialización aquí
    private String motivoAnulacion;
}
// numero de comprobante y estado