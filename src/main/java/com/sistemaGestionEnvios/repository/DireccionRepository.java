package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.Direccion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    public List<Direccion> findByProvincia(String provincia);

    public List<Direccion> findByCanton(String canton);
}