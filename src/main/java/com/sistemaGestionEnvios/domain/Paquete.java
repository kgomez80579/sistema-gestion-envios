package com.sistemaGestionEnvios.domain;
 
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
 
@Data
@Entity
@Table(name = "paquete")
public class Paquete implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private Integer idPaquete;
 
    @Column(nullable = false, length = 150)
    private String descripcion;
 
    @Column(precision = 10, scale = 2)
    private BigDecimal peso;
 
    @Column(length = 100)
    private String dimensiones;
 
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
 
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}