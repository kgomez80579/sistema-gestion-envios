package com.sistemaGestionEnvios.domain;
 
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
 
@Data
@Entity
@Table(name = "repartidor")
public class Repartidor implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repartidor")
    private Integer idRepartidor;
 
    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
 
    @Column(length = 100)
    private String vehiculo;
 
    @Column(length = 30)
    private String estado;
    @Column(name = "foto_url", length = 2048)
    private String fotoUrl;
 
    @Column(name = "licencia_url", length = 2048)
    private String licenciaUrl;
 
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
 
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;   
}