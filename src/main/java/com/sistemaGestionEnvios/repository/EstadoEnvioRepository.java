package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoEnvioRepository extends JpaRepository<EstadoEnvio, Integer> {

    public EstadoEnvio findByNombreEstado(String nombreEstado);
}
