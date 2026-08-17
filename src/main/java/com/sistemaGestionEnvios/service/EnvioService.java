package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Envio;
import com.sistemaGestionEnvios.domain.EstadoEnvio;
import com.sistemaGestionEnvios.repository.EnvioRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final EstadoEnvioService estadoEnvioService;

    public EnvioService(EnvioRepository envioRepository,
            EstadoEnvioService estadoEnvioService) {
        this.envioRepository = envioRepository;
        this.estadoEnvioService = estadoEnvioService;
    }

    @Transactional(readOnly = true)
    public List<Envio> getEnvios() {
        return envioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Envio> getEnvio(Integer idEnvio) {
        return envioRepository.findById(idEnvio);
    }

    @Transactional(readOnly = true)
    public List<Envio> getEnviosPorCliente(Integer idCliente) {
        return envioRepository.findByClienteIdCliente(idCliente);
    }

    @Transactional(readOnly = true)
    public List<Envio> getEnviosPorEstado(Integer idEstado) {
        return envioRepository.findByEstadoEnvioIdEstado(idEstado);
    }
    
    @Transactional(readOnly = true)
    public boolean existeEnvioParaSolicitud(Integer idSolicitud) {
        return envioRepository.existsBySolicitudIdSolicitud(idSolicitud);
    }

    @Transactional(readOnly = true)
    public Envio getEnvioPorCodigo(String codigoSeguimiento) {
        return envioRepository.findByCodigoSeguimiento(codigoSeguimiento);
    }

    @Transactional(readOnly = true)
    public List<Envio> getEnviosPorRepartidor(Integer idRepartidor) {
        return envioRepository.findByRepartidorIdRepartidor(idRepartidor);
    }

    @Transactional
    public void save(Envio envio) {
        if (envio.getRepartidor() != null
                && envio.getRepartidor().getIdRepartidor() == null) {
            envio.setRepartidor(null);
        }
        if (envio.getIdEnvio() == null) {
            EstadoEnvio estadoInicial
                    = estadoEnvioService.getEstadoPorNombre("Registrado");
            if (estadoInicial == null) {
                throw new IllegalStateException(
                        "No existe el estado inicial 'Registrado'.");
            }
            envio.setEstadoEnvio(estadoInicial);
            if (envio.getCodigoSeguimiento() == null
                    || envio.getCodigoSeguimiento().isBlank()) {
                envio.setCodigoSeguimiento(generarCodigoSeguimiento());
            }
        }
        envioRepository.save(envio);
    }

    @Transactional
    public void delete(Integer idEnvio) {
        if (!envioRepository.existsById(idEnvio)) {
            throw new IllegalArgumentException(
                    "El envío con ID " + idEnvio + " no existe.");
        }
        try {
            envioRepository.deleteById(idEnvio);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el envío. Tiene historial asociado.", e);
        }
    }

    @Transactional
    public void actualizarEstado(Integer idEnvio, Integer idEstado,
            String observacion, Integer idRepartidorSolicitante) {

        Envio envio = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new IllegalArgumentException(
                "El envío con ID " + idEnvio + " no existe."));

        if (idRepartidorSolicitante != null) {
            if (envio.getRepartidor() == null) {
                throw new IllegalStateException(
                        "Este envío no tiene un repartidor asignado.");
            }
            if (!envio.getRepartidor().getIdRepartidor().equals(idRepartidorSolicitante)) {
                throw new IllegalStateException(
                        "No tiene permisos para actualizar este envío.");
            }
        }

        EstadoEnvio estadoNuevo = estadoEnvioService.getEstadoEnvio(idEstado)
                .orElseThrow(() -> new IllegalArgumentException(
                "El estado indicado no existe."));

        envio.setEstadoEnvio(estadoNuevo);
        if (observacion != null && !observacion.isBlank()) {
            envio.setObservacion(observacion);
        }
        envioRepository.save(envio);
    }

    private String generarCodigoSeguimiento() {
        String fecha = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ENV-" + fecha;
    }
    
    
}
