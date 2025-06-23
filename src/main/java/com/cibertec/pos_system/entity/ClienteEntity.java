package com.cibertec.pos_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "clientes")
@Data
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    private boolean activo;

    /*Nuevo: entidad para que funcione sistema de puntos */
    private String tipo; /*Ejemplo: "regular", "vip", etc */

    /*Nuevo: Puntos acumulados */
    private Integer puntosAcumulados = 0;

}
