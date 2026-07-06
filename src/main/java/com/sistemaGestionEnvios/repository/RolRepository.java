package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    public Rol findByNombreRol(String nombreRol);
}