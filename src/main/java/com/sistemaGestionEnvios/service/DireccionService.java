package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Direccion;
import com.sistemaGestionEnvios.repository.DireccionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    @Transactional(readOnly = true)
    public List<Direccion> getDirecciones() {
        return direccionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Direccion> getDireccion(Integer idDireccion) {
        return direccionRepository.findById(idDireccion);
    }

    @Transactional(readOnly = true)
    public List<Direccion> getDireccionesPorProvincia(String provincia) {
        return direccionRepository.findByProvincia(provincia);
    }

    @Transactional(readOnly = true)
    public List<Direccion> getDireccionesPorCanton(String canton) {
        return direccionRepository.findByCanton(canton);
    }

    @Transactional
    public void save(Direccion direccion) {
        direccionRepository.save(direccion);
    }

    @Transactional
    public void delete(Integer idDireccion) {
        if (!direccionRepository.existsById(idDireccion)) {
            throw new IllegalArgumentException("La dirección con ID " + idDireccion + " no existe.");
        }

        try {
            direccionRepository.deleteById(idDireccion);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la dirección. Tiene datos asociados.", e);
        }
    }
}
