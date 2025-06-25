package com.cibertec.pos_system.model.ventas;


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
    private double precio;
    private double subtotal;
    private int quantity;

    
    // constructor, getters, setters
}
