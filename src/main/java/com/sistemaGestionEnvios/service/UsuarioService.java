package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Usuario;
import com.sistemaGestionEnvios.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activo) {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioPorEmail(String email) {
        return usuarioRepository.findByCorreo(email);
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> getUsuariosPorRol(String nombreRol) {
        return usuarioRepository.findByRol_NombreRol(nombreRol);
    }

    @Transactional
    public void save(Usuario usuario) {

        if (usuario.getIdUsuario() != null) {
            Usuario usuarioActual = usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);

            if (usuarioActual != null) {
                if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                    usuario.setPassword(usuarioActual.getPassword());
                }
            }
        }

        if (usuario.getEstado() == null || usuario.getEstado().isBlank()) {
            usuario.setEstado("Activo");
        }

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            if (!usuario.getPassword().startsWith("$2a$")
                    && !usuario.getPassword().startsWith("$2b$")
                    && !usuario.getPassword().startsWith("$2y$")) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarActivo(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe."));

        if ("Activo".equalsIgnoreCase(usuario.getEstado())) {
            usuario.setEstado("Inactivo");
        } else {
            usuario.setEstado("Activo");
        }

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe.");
        }

        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }
}