package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "rutas")
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Integer idRuta;

    @Column(name = "nombre_ruta", nullable = false, length = 100)
    @NotBlank(message = "El nombre de la ruta no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la ruta no puede tener más de 100 caracteres.")
    private String nombreRuta;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "La zona no puede estar vacía.")
    @Size(max = 100, message = "La zona no puede tener más de 100 caracteres.")
    private String zona;

    @Column(name = "fecha_ruta", nullable = false)
    @NotNull(message = "La fecha de la ruta no puede estar vacía.")
    private LocalDate fechaRuta;

    @ManyToOne
    @JoinColumn(name = "id_repartidor", nullable = false)
    private Repartidor repartidor;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
