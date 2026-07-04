package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Paquete;
import com.sistemaGestionEnvios.repository.PaqueteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;

    public PaqueteService(PaqueteRepository paqueteRepository) {
        this.paqueteRepository = paqueteRepository;
    }

    @Transactional(readOnly = true)
    public List<Paquete> getPaquetes(boolean activo) {
        if (activo) {
            return paqueteRepository.findByActivoTrue();
        }
        return paqueteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Paquete> getPaquete(Integer idPaquete) {
        return paqueteRepository.findById(idPaquete);
    }

    @Transactional
    public void save(Paquete paquete) {
        paqueteRepository.save(paquete);
    }

    @Transactional
    public void cambiarActivo(Integer idPaquete) {
        Paquete paquete = paqueteRepository.findById(idPaquete)
                .orElseThrow(() -> new IllegalArgumentException("El paquete con ID " + idPaquete + " no existe."));

        paquete.setActivo(!paquete.getActivo());
        paqueteRepository.save(paquete);
    }

    @Transactional
    public void delete(Integer idPaquete) {
        if (!paqueteRepository.existsById(idPaquete)) {
            throw new IllegalArgumentException("El paquete con ID " + idPaquete + " no existe.");
        }

        try {
            paqueteRepository.deleteById(idPaquete);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el paquete. Tiene datos asociados.", e);
        }
    }
}
