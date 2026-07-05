package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Repartidor;
import com.sistemaGestionEnvios.repository.RepartidorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepartidorService {

    private final RepartidorRepository repartidorRepository;

    public RepartidorService(RepartidorRepository repartidorRepository) {
        this.repartidorRepository = repartidorRepository;
    }

    @Transactional(readOnly = true)
    public List<Repartidor> getRepartidores() {
        return repartidorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Repartidor> getRepartidoresDisponibles() {
        return repartidorRepository.findByEstado("Disponible");
    }

    @Transactional(readOnly = true)
    public Optional<Repartidor> getRepartidor(Integer idRepartidor) {
        return repartidorRepository.findById(idRepartidor);
    }

    @Transactional
    public void save(Repartidor repartidor) {
        repartidorRepository.save(repartidor);
    }

    @Transactional
    public void delete(Integer idRepartidor) {
        if (!repartidorRepository.existsById(idRepartidor)) {
            throw new IllegalArgumentException("El repartidor con ID " + idRepartidor + " no existe.");
        }

        try {
            repartidorRepository.deleteById(idRepartidor);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el repartidor. Tiene datos asociados.", e);
        }
    }
}
