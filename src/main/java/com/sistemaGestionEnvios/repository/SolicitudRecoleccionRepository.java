package com.sistemaGestionEnvios.repository;

import com.sistemaGestionEnvios.domain.SolicitudRecoleccion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRecoleccionRepository extends JpaRepository <SolicitudRecoleccion, Integer>{
    
    public List<SolicitudRecoleccion> findByClienteIdClienteOrderByIdSolicitudDesc(Integer idCliente);
    
    public List<SolicitudRecoleccion> findByRepartidorIdRepartidorOrderByIdSolicitudDesc(Integer idRepartidor);

    public List<SolicitudRecoleccion> findByEstadoOrderByIdSolicitudDesc(String estado);

    public List<SolicitudRecoleccion> findAllByOrderByIdSolicitudDesc();
}
