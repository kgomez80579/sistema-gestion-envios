package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "envios")
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

    @ManyToOne
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

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
    @Size(max = 30, message = "El código de seguimiento no puede tener más de 30 caracteres.")
    private String codigoSeguimiento;

    @Column(name = "nombre_destinatario", length = 100)
    @Size(max = 100, message = "El nombre del destinatario no puede tener más de 100 caracteres.")
    private String nombreDestinatario;

    @Column(name = "telefono_destinatario", length = 25)
    @Size(max = 25, message = "El teléfono del destinatario no puede tener más de 25 caracteres.")
    private String telefonoDestinatario;

    @Column(name = "fecha_recoleccion_estimada")
    private LocalDateTime fechaRecoleccionEstimada;

    @Column(name = "fecha_envio", insertable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @Column(length = 255)
    @Size(max = 255, message = "La observación no puede tener más de 255 caracteres.")
    private String observacion;

    @Column(length = 20)
    private String estado;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        if (estado == null || estado.isBlank()) {
            estado = "Activo";
        }
    }
}
