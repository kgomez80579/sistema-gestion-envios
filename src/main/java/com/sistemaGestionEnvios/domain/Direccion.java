package com.sistemaGestionEnvios.domain;
 
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
 
@Data
@Entity
@Table(name = "direccion")
public class Direccion implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;
 
    @Column(nullable = false, length = 50)
    private String provincia;
 
    @Column(nullable = false, length = 50)
    private String canton;
 
    @Column(nullable = false, length = 50)
    private String distrito;
 
    @Column(name = "direccion_exacta", nullable = false, length = 255)
    private String direccionExacta;
 
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
 
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}