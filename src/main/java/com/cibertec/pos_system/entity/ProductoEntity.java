package com.cibertec.pos_system.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "productos")
@Data
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
    private int stock; //  cantidad disponible

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntity categoria;
}
