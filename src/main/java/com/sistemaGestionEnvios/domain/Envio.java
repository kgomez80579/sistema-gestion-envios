package com.sistemaGestionEnvios.domain;
 
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
 
@Data
@Entity
@Table(name = "envio")
public class Envio implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Integer idEnvio;
 
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
 
    @ManyToOne
    @JoinColumn(name = "id_paquete", nullable = false)
    private Paquete paquete;
 
    @ManyToOne
    @JoinColumn(name = "id_repartidor")
    private Repartidor repartidor;
 
    @OneToOne
    @JoinColumn(name = "id_solicitud", unique = true)
    private SolicitudRecoleccion solicitud;
    
    @ManyToOne
    @JoinColumn(name = "id_direccion_origen", nullable = false)
    private Direccion direccionOrigen;
 
    @ManyToOne
    @JoinColumn(name = "id_direccion_destino", nullable = false)
    private Direccion direccionDestino;
 
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoEnvio estadoEnvio;
 
    @Column(name = "codigo_seguimiento", nullable = false, unique = true, length = 30)
    private String codigoSeguimiento;
 
    @Column(name = "nombre_destinatario", length = 100)
    private String nombreDestinatario;
 
    @Column(name = "telefono_destinatario", length = 25)
    private String telefonoDestinatario;
 
    @Column(name = "fecha_recoleccion_estimada")
    private LocalDateTime fechaRecoleccionEstimada;
 
    @Column(name = "fecha_envio", insertable = false, updatable = false)
    private LocalDateTime fechaEnvio;
 
    @Column(length = 255)
    private String observacion;
 
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
 
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}