package com.cibertec.pos_system.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cibertec.pos_system.enums.MetodoPago;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VentaWeb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroComprobante;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    private ClienteEntity cliente; // El cliente que compra, si está registrado

    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    private String estado;

    @OneToMany(mappedBy = "ventaWeb", cascade = CascadeType.ALL)
    private List<VentaDetalleWeb> detalles = new ArrayList<>();
}

