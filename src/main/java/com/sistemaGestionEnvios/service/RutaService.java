package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Ruta;
import com.sistemaGestionEnvios.repository.RutaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @Transactional(readOnly = true)
    public List<Ruta> getRutas() {
        return rutaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ruta> getRuta(Integer idRuta) {
        return rutaRepository.findById(idRuta);
    }

    @Transactional(readOnly = true)
    public List<Ruta> getRutasPorRepartidor(Integer idRepartidor) {
        return rutaRepository.findByRepartidorIdRepartidor(idRepartidor);
    }

    @Transactional
    public void save(Ruta ruta) {
        rutaRepository.save(ruta);
    }

    @Transactional
    public void delete(Integer idRuta) {
        if (!rutaRepository.existsById(idRuta)) {
            throw new IllegalArgumentException("La ruta con ID " + idRuta + " no existe.");
        }

        try {
            rutaRepository.deleteById(idRuta);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la ruta. Tiene datos asociados.", e);
        }
    }
}
