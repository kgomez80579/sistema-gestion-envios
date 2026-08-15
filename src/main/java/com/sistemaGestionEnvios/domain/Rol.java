package com.sistemaGestionEnvios.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;
// Añadir restricción de longitud y unicidad si el campo 'rol' es el nombre del rol
    @Column(name = "rol", unique = true, length = 25)
    private String rol;
}