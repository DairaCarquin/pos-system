package com.cibertec.pos_system.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrito {
    private Long productoId;
    private String nombre;
    private String imagen;
    private BigDecimal precio;
    private double subtotal;
    private int quantity;

    
    public double calcularSubTotal() {
        return precio.multiply(BigDecimal.valueOf(quantity)).doubleValue();
    }
}
