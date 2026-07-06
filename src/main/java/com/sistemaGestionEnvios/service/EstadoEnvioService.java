package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.EstadoEnvio;
import com.sistemaGestionEnvios.repository.EstadoEnvioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadoEnvioService {

    private final EstadoEnvioRepository estadoEnvioRepository;

    public EstadoEnvioService(EstadoEnvioRepository estadoEnvioRepository) {
        this.estadoEnvioRepository = estadoEnvioRepository;
    }

    @Transactional(readOnly = true)
    public List<EstadoEnvio> getEstadosEnvio() {
        return estadoEnvioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EstadoEnvio> getEstadoEnvio(Integer idEstado) {
        return estadoEnvioRepository.findById(idEstado);
    }

    @Transactional(readOnly = true)
    public EstadoEnvio getEstadoPorNombre(String nombreEstado) {
        return estadoEnvioRepository.findByNombreEstado(nombreEstado);
    }
}
