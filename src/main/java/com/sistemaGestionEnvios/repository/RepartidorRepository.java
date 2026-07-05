package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.Repartidor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Integer> {

    public List<Repartidor> findByEstado(String estado);
}
