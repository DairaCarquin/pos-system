package com.cibertec.pos_system.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VentaDetalleWeb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la venta principal
    @ManyToOne
    @JoinColumn(name = "venta_web_id", nullable = false)
    private VentaWeb ventaWeb;

    // Relación con el producto vendido
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;
    private String nombreProducto; // opcional, útil para historial

    // Campos del detalle
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

}