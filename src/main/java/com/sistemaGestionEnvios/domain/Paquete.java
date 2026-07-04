package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "paquetes")
public class Paquete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private Integer idPaquete;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "La descripción del paquete no puede estar vacía.")
    @Size(max = 150, message = "La descripción no puede tener más de 150 caracteres.")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "El peso no puede estar vacío.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El peso debe ser mayor a 0.")
    private BigDecimal peso;

    @Column(length = 100)
    @Size(max = 100, message = "Las dimensiones no pueden tener más de 100 caracteres.")
    private String dimensiones;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El estado no puede estar vacío.")
    @Size(max = 50, message = "El estado no puede tener más de 50 caracteres.")
    private String estado;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }

        if (activo == null) {
            activo = true;
        }

        if (estado == null || estado.isBlank()) {
            estado = "Registrado";
        }
    }
}
