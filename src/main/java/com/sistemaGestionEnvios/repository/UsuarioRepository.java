package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByCorreo(String correo);
    
    public List<Usuario> findByRol_NombreRol(String nombreRol);
}