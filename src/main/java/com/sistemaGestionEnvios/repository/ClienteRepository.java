package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    public Optional<Cliente> findByUsuarioIdUsuario(Integer idUsuario);
}