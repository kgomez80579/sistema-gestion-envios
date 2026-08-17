package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Repartidor;
import com.sistemaGestionEnvios.domain.SolicitudRecoleccion;
import com.sistemaGestionEnvios.repository.SolicitudRecoleccionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudRecoleccionService {
    
    public static final String ESTADO_PENDIENTE = "Pendiente de recolección";
    public static final String ESTADO_APROBADA = "Aprobada";
    public static final String ESTADO_RECHAZADA = "Rechazada";
    public static final String ESTADO_CANCELADA = "Cancelada";

    private final SolicitudRecoleccionRepository solicitudRecoleccionRepository;

    public SolicitudRecoleccionService(SolicitudRecoleccionRepository solicitudRecoleccionRepository) {
        this.solicitudRecoleccionRepository = solicitudRecoleccionRepository;
    }

    @Transactional(readOnly = true)
    public List<SolicitudRecoleccion> getSolicitudes() {
        return solicitudRecoleccionRepository.findAllByOrderByIdSolicitudDesc();
    }

    @Transactional(readOnly = true)
    public List<SolicitudRecoleccion> getSolicitudesPorCliente(Integer idCliente) {
        return solicitudRecoleccionRepository.findByClienteIdClienteOrderByIdSolicitudDesc(idCliente);
    }

    @Transactional(readOnly = true)
    public List<SolicitudRecoleccion> getSolicitudesPorRepartidor(Integer idRepartidor) {
        return solicitudRecoleccionRepository.findByRepartidorIdRepartidorOrderByIdSolicitudDesc(idRepartidor);
    }

    @Transactional(readOnly = true)
    public List<SolicitudRecoleccion> getSolicitudesPorEstado(String estado) {
        return solicitudRecoleccionRepository.findByEstadoOrderByIdSolicitudDesc(estado);
    }

    @Transactional(readOnly = true)
    public Optional<SolicitudRecoleccion> getSolicitud(Integer idSolicitud) {
        return solicitudRecoleccionRepository.findById(idSolicitud);
    }

    @Transactional
    public void save(SolicitudRecoleccion solicitud) {
        if (solicitud.getIdSolicitud() == null) {
            solicitud.setEstado(ESTADO_PENDIENTE);
            solicitud.setRepartidor(null);
            solicitud.setMotivoRechazo(null);
        }
        solicitudRecoleccionRepository.save(solicitud);
    }

    @Transactional
    public void aprobar(Integer idSolicitud, Repartidor repartidor) {
        SolicitudRecoleccion solicitud = solicitudRecoleccionRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La solicitud con ID " + idSolicitud + " no existe."));
        if (!ESTADO_PENDIENTE.equals(solicitud.getEstado())) {
            throw new IllegalStateException(
                    "Solo se pueden aprobar solicitudes pendientes de recolección.");
        }
        solicitud.setEstado(ESTADO_APROBADA);
        solicitud.setRepartidor(repartidor);
        solicitud.setMotivoRechazo(null);
        solicitudRecoleccionRepository.save(solicitud);
    }

    @Transactional
    public void rechazar(Integer idSolicitud, String motivoRechazo) {
        SolicitudRecoleccion solicitud = solicitudRecoleccionRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La solicitud con ID " + idSolicitud + " no existe."));
        if (!ESTADO_PENDIENTE.equals(solicitud.getEstado())) {
            throw new IllegalStateException(
                    "Solo se pueden rechazar solicitudes pendientes de recolección.");
        }
        solicitud.setEstado(ESTADO_RECHAZADA);
        solicitud.setMotivoRechazo(motivoRechazo);
        solicitudRecoleccionRepository.save(solicitud);
    }

    @Transactional
    public void cancelar(Integer idSolicitud, Integer idCliente) {
        SolicitudRecoleccion solicitud = solicitudRecoleccionRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La solicitud con ID " + idSolicitud + " no existe."));
        if (!solicitud.getCliente().getIdCliente().equals(idCliente)) {
            throw new IllegalStateException("No tiene permisos para cancelar esta solicitud.");
        }
        if (!ESTADO_PENDIENTE.equals(solicitud.getEstado())) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar solicitudes pendientes de recolección.");
        }
        solicitud.setEstado(ESTADO_CANCELADA);
        solicitudRecoleccionRepository.save(solicitud);
    }

    @Transactional
    public void delete(Integer idSolicitud) {
        if (!solicitudRecoleccionRepository.existsById(idSolicitud)) {
            throw new IllegalArgumentException(
                    "La solicitud con ID " + idSolicitud + " no existe.");
        }
        try {
            solicitudRecoleccionRepository.deleteById(idSolicitud);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar la solicitud. Tiene datos asociados.", e);
        }
    }   
}
