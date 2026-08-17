
package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "solicitud_recoleccion")
public class SolicitudRecoleccion implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_direccion_origen", nullable = false)
    @NotNull
    private Direccion direccionOrigen;

    @ManyToOne
    @JoinColumn(name = "id_repartidor")
    private Repartidor repartidor;

    @NotBlank
    @Column(name = "descripcion_paquete", nullable = false, length = 150)
    private String descripcionPaquete;

    @NotNull
    @Column(name = "fecha_hora_estimada", nullable = false)
    private LocalDateTime fechaHoraEstimada;

    @Column(length = 30)
    private String estado;

    @Column(name = "motivo_rechazo", length = 255)
    private String motivoRechazo;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
    
    private transient Boolean envioGenerado;
}
