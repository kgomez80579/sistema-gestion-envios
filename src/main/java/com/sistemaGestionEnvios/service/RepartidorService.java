package com.sistemaGestionEnvios.service;

import com.sistemaGestionEnvios.domain.Repartidor;
import com.sistemaGestionEnvios.repository.RepartidorRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RepartidorService {

    private final RepartidorRepository repartidorRepository;
    private final FirebaseStorageService firebaseStorageService;

    
    public RepartidorService(RepartidorRepository repartidorRepository,
            FirebaseStorageService firebaseStorageService) {
        this.repartidorRepository = repartidorRepository;
        this.firebaseStorageService = firebaseStorageService;
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
        try {
            repartidorRepository.deleteById(idRepartidor);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Transactional
    public void guardarImagenes(Integer idRepartidor, MultipartFile fotoFile, MultipartFile licenciaFile) throws IOException {
        Repartidor repartidor = repartidorRepository.findById(idRepartidor)
                .orElseThrow(() -> new IllegalArgumentException("El repartidor con ID " + idRepartidor + " no existe."));

        String fotoUrl = firebaseStorageService.uploadImage(fotoFile, "fotos", idRepartidor);
        String licenciaUrl = firebaseStorageService.uploadImage(licenciaFile, "licencias", idRepartidor);

        if (fotoUrl != null) {
            repartidor.setFotoUrl(fotoUrl);
        }

        if (licenciaUrl != null) {
            repartidor.setLicenciaUrl(licenciaUrl);
        }

        repartidorRepository.save(repartidor);
    }
}
