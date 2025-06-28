package com.cibertec.pos_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "precio_descuento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecioDescuentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descuento_id")
    private DescuentoEntity descuento;

    @Column(name = "precio_final", nullable = false)
    private Double precioFinal;

    @Column(name = "fecha_aplicacion", nullable = false)
    private LocalDate fechaAplicacion;

    @Column(nullable = false)
    private boolean vigente;
}
