package com.cibertec.pos_system.puntos.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "puntos_ventas_procesadas")
@Data
public class VentaProcesadaPuntos {

    @Id
    private Long ventaId; // Usamos el mismo ID de la tabla de ventas de Caja

    private LocalDateTime fechaProcesamiento;

    private int puntosAsignados;
}