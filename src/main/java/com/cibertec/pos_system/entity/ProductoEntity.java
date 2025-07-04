package com.cibertec.pos_system.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
@Table(name = "productos")
@Data
@ToString
@Entity
public class ProductoEntity {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;
    private boolean activo;
    private int stockActual;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @ToString.Exclude
    private CategoriaEntity categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    @ToString.Exclude
    private ProveedorEntity proveedor;

    @Transient
    @ToString.Exclude
    private DescuentoEntity descuentoAplicado;

    @Transient
    @ToString.Exclude
    private BigDecimal montoDescuento;
}
