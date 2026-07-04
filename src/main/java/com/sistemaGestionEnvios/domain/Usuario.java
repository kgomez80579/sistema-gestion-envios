package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 80, message = "Los apellidos no pueden tener más de 80 caracteres.")
    private String apellidos;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "Debe ingresar un correo válido.")
    @Size(max = 100, message = "El correo no puede tener más de 100 caracteres.")
    private String correo;

    @Column(nullable = false, length = 512)
    @Size(max = 512, message = "La contraseña no puede tener más de 512 caracteres.")
    private String password;

    @Column(length = 25)
    @Size(max = 25, message = "El teléfono no puede tener más de 25 caracteres.")
    private String telefono;

    @Column(length = 20)
    private String estado;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @PrePersist
    public void prePersist() {
        if (estado == null || estado.isBlank()) {
            estado = "Activo";
        }
    }
}