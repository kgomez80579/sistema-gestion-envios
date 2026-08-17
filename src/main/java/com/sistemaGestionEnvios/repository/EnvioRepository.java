package com.sistemaGestionEnvios.repository;
 
import com.sistemaGestionEnvios.domain.Envio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
 
    public List<Envio> findByClienteIdCliente(Integer idCliente);
 
    public List<Envio> findByEstadoEnvioIdEstado(Integer idEstado);
 
    public Envio findByCodigoSeguimiento(String codigoSeguimiento);
    
    public boolean existsBySolicitudIdSolicitud(Integer idSolicitud);
    
    public List<Envio> findByRepartidorIdRepartidor(Integer idRepartidor);
}