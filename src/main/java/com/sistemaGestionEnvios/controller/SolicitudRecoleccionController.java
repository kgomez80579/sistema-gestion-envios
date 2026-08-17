package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Cliente;
import com.sistemaGestionEnvios.domain.Repartidor;
import com.sistemaGestionEnvios.domain.SolicitudRecoleccion;
import com.sistemaGestionEnvios.domain.Usuario;
import com.sistemaGestionEnvios.service.ClienteService;
import com.sistemaGestionEnvios.service.DireccionService;
import com.sistemaGestionEnvios.service.RepartidorService;
import com.sistemaGestionEnvios.service.SolicitudRecoleccionService;
import com.sistemaGestionEnvios.service.UsuarioService;
import com.sistemaGestionEnvios.service.EnvioService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/solicitud-recoleccion")
public class SolicitudRecoleccionController {
    
private final SolicitudRecoleccionService solicitudRecoleccionService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final RepartidorService repartidorService;
    private final DireccionService direccionService;
    private final EnvioService envioService;
    private final MessageSource messageSource;

    public SolicitudRecoleccionController(
            SolicitudRecoleccionService solicitudRecoleccionService,
            ClienteService clienteService,
            UsuarioService usuarioService,
            RepartidorService repartidorService,
            DireccionService direccionService,
            EnvioService envioService,
            MessageSource messageSource) {
        this.solicitudRecoleccionService = solicitudRecoleccionService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
        this.repartidorService = repartidorService;
        this.direccionService = direccionService;
        this.envioService = envioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) String estado,
            Model model, Authentication authentication) {

        List<SolicitudRecoleccion> solicitudes;

        if (esAdmin(authentication)) {

            if (estado != null && !estado.isBlank()) {
                solicitudes = solicitudRecoleccionService.getSolicitudesPorEstado(estado);
            } else {
                solicitudes = solicitudRecoleccionService.getSolicitudes();
            }

            model.addAttribute("repartidores", repartidorService.getRepartidoresDisponibles());

            for (SolicitudRecoleccion s : solicitudes) {
                boolean tieneEnvio = envioService.existeEnvioParaSolicitud(s.getIdSolicitud());
                s.setEnvioGenerado(tieneEnvio);
            }

        } else {
            Optional<Repartidor> repartidorOpt = getRepartidorActual(authentication);

            if (repartidorOpt.isPresent()) {
                solicitudes = solicitudRecoleccionService.getSolicitudesPorRepartidor(
                        repartidorOpt.get().getIdRepartidor());
            } else {
                Optional<Cliente> clienteOpt = getClienteActual(authentication);

                if (clienteOpt.isPresent()) {
                    Cliente cliente = clienteOpt.get();
                    solicitudes = solicitudRecoleccionService.getSolicitudesPorCliente(cliente.getIdCliente());
                } else {
                    solicitudes = Collections.emptyList();
                }
            }
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("totalSolicitudes", solicitudes.size());
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("direcciones", direccionService.getDirecciones());
        model.addAttribute("solicitud", new SolicitudRecoleccion());
        return "/solicitud_recoleccion/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid SolicitudRecoleccion solicitud,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Optional<Cliente> clienteOpt = getClienteActual(authentication);
        if (clienteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("solicitud.error04", null, Locale.getDefault()));
            return "redirect:/solicitud-recoleccion/listado";
        }

        solicitud.setCliente(clienteOpt.get());
        solicitudRecoleccionService.save(solicitud);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/solicitud-recoleccion/listado";
    }

    @PostMapping("/aprobar")
    public String aprobar(@RequestParam Integer idSolicitud,
            @RequestParam Integer idRepartidor,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "solicitud.aprobada";
        try {
            Repartidor repartidor = repartidorService.getRepartidor(idRepartidor)
                    .orElseThrow(() -> new IllegalArgumentException("El repartidor indicado no existe."));
            solicitudRecoleccionService.aprobar(idSolicitud, repartidor);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "solicitud.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "solicitud.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "solicitud.error03";
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/solicitud-recoleccion/listado";
    }

    @PostMapping("/rechazar")
    public String rechazar(@RequestParam Integer idSolicitud,
            @RequestParam String motivoRechazo,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "solicitud.rechazada";
        try {
            solicitudRecoleccionService.rechazar(idSolicitud, motivoRechazo);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "solicitud.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "solicitud.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "solicitud.error03";
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/solicitud-recoleccion/listado";
    }

    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idSolicitud,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "solicitud.cancelada";
        try {
            Cliente cliente = getClienteActual(authentication)
                    .orElseThrow(() -> new IllegalStateException("No tiene un perfil de cliente asociado."));
            solicitudRecoleccionService.cancelar(idSolicitud, cliente.getIdCliente());
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "solicitud.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "solicitud.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "solicitud.error03";
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/solicitud-recoleccion/listado";
    }

    private Optional<Cliente> getClienteActual(Authentication authentication) {
        if (authentication == null) {
            return Optional.empty();
        }
        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorUsername(authentication.getName());
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }
        return clienteService.getClientePorUsuario(usuarioOpt.get().getIdUsuario());
    }

    private Optional<Repartidor> getRepartidorActual(Authentication authentication) {
    if (authentication == null) {
        return Optional.empty();
    }
    Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorUsername(authentication.getName());
    if (usuarioOpt.isEmpty()) {
        return Optional.empty();
    }
    return repartidorService.getRepartidorPorUsuario(usuarioOpt.get().getIdUsuario());
}
    //Funcion para verificar si el usuario auntenticado tiene el rol admin
    private boolean esAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        //recorre los roles que tiene el usuario
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

}