package com.sistemaGestionEnvios.domain;
 
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
 
@Data
@Entity
@Table(name = "estadoEnvio")
public class EstadoEnvio implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;
 
    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    private String nombreEstado;
 
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
 
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}