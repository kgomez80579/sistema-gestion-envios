package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.Paquete;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {

    public List<Paquete> findByActivoTrue();
}
