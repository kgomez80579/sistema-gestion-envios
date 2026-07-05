package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "direcciones")
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La provincia no puede estar vacía.")
    @Size(max = 50, message = "La provincia no puede tener más de 50 caracteres.")
    private String provincia;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El cantón no puede estar vacío.")
    @Size(max = 50, message = "El cantón no puede tener más de 50 caracteres.")
    private String canton;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El distrito no puede estar vacío.")
    @Size(max = 50, message = "El distrito no puede tener más de 50 caracteres.")
    private String distrito;

    @Column(name = "direccion_exacta", nullable = false, length = 255)
    @NotBlank(message = "La dirección exacta no puede estar vacía.")
    @Size(max = 255, message = "La dirección exacta no puede tener más de 255 caracteres.")
    private String direccionExacta;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}