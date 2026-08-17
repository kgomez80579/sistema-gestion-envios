package com.sistemaGestionEnvios.controller;
 
import com.sistemaGestionEnvios.domain.Cliente;
import com.sistemaGestionEnvios.domain.Envio;
import com.sistemaGestionEnvios.domain.Repartidor;
import com.sistemaGestionEnvios.domain.SolicitudRecoleccion;
import com.sistemaGestionEnvios.domain.Usuario;
import com.sistemaGestionEnvios.service.ClienteService;
import com.sistemaGestionEnvios.service.DireccionService;
import com.sistemaGestionEnvios.service.EnvioService;
import com.sistemaGestionEnvios.service.EstadoEnvioService;
import com.sistemaGestionEnvios.service.PaqueteService;
import com.sistemaGestionEnvios.service.RepartidorService;
import com.sistemaGestionEnvios.service.SolicitudRecoleccionService;
import com.sistemaGestionEnvios.service.UsuarioService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/envio")
public class EnvioController {
 
    private final EnvioService envioService;
    private final ClienteService clienteService;
    private final PaqueteService paqueteService;
    private final RepartidorService repartidorService;
    private final DireccionService direccionService;
    private final EstadoEnvioService estadoEnvioService;
    private final SolicitudRecoleccionService solicitudRecoleccionService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public EnvioController(EnvioService envioService,
            ClienteService clienteService,
            PaqueteService paqueteService,
            RepartidorService repartidorService,
            DireccionService direccionService,
            EstadoEnvioService estadoEnvioService,
            SolicitudRecoleccionService solicitudRecoleccionService,
            UsuarioService usuarioService,
            MessageSource messageSource) {
        this.envioService = envioService;
        this.clienteService = clienteService;
        this.paqueteService = paqueteService;
        this.repartidorService = repartidorService;
        this.direccionService = direccionService;
        this.estadoEnvioService = estadoEnvioService;
        this.solicitudRecoleccionService = solicitudRecoleccionService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model, Authentication authentication) {

        List<Envio> envios;

        if (esAdmin(authentication)) {
            envios = envioService.getEnvios();

        } else {
            Optional<Repartidor> repartidorOpt = getRepartidorActual(authentication);

            if (repartidorOpt.isPresent()) {
                Repartidor repartidor = repartidorOpt.get();
                envios = envioService.getEnviosPorRepartidor(repartidor.getIdRepartidor());

            } else {
                Optional<Cliente> clienteOpt = getClienteActual(authentication);

                if (clienteOpt.isPresent()) {
                    Cliente cliente = clienteOpt.get();
                    envios = envioService.getEnviosPorCliente(cliente.getIdCliente());
                } else {
                    envios = Collections.emptyList();
                }
            }
        }

        model.addAttribute("envios", envios);
        model.addAttribute("totalEnvios", envios.size());
        cargarListasFormulario(model);
        model.addAttribute("envio", new Envio());
        return "/envio/listado";
    }

    @GetMapping("/filtrar")
    public String filtrar(@RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) Integer idEstado,
            Model model) {
        var envios = envioService.getEnvios();
        if (idCliente != null) {
            envios = envioService.getEnviosPorCliente(idCliente);
        }
        if (idEstado != null) {
            envios = envioService.getEnviosPorEstado(idEstado);
        }
        model.addAttribute("envios", envios);
        model.addAttribute("totalEnvios", envios.size());
        model.addAttribute("idClienteSeleccionado", idCliente);
        model.addAttribute("idEstadoSeleccionado", idEstado);
        cargarListasFormulario(model);
        model.addAttribute("envio", new Envio());
        return "/envio/listado";
    }
 
    @PostMapping("/guardar")
    public String guardar(@Valid Envio envio,
            RedirectAttributes redirectAttributes) {
        envioService.save(envio);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/envio/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idEnvio,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            envioService.delete(idEnvio);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "envio.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "envio.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "envio.error03";
        }
        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/envio/listado";
    }
    
    @PostMapping("/actualizar-estado")
    public String actualizarEstado(@RequestParam Integer idEnvio,
            @RequestParam Integer idEstado,
            @RequestParam(required = false) String observacion,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "envio.estadoActualizado";
        try {
            Integer idRepartidorSolicitante = null;

            if (!esAdmin(authentication)) {
                Repartidor repartidor = getRepartidorActual(authentication)
                        .orElseThrow(() -> new IllegalStateException(
                                "No tiene un perfil de repartidor asociado."));
                idRepartidorSolicitante = repartidor.getIdRepartidor();
            }

            envioService.actualizarEstado(idEnvio, idEstado, observacion, idRepartidorSolicitante);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "envio.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "envio.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "envio.error03";
        }
        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/envio/listado";
    }

    @GetMapping("/modificar/{idEnvio}")
    public String modificar(@PathVariable("idEnvio") Integer idEnvio,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Envio> envioOpt = envioService.getEnvio(idEnvio);
        if (envioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("envio.error01", null, Locale.getDefault())
            );
            return "redirect:/envio/listado";
        }
        model.addAttribute("envio", envioOpt.get());
        cargarListasFormulario(model);
        return "/envio/modifica";
    }
    
        @GetMapping("/nuevo-desde-solicitud/{idSolicitud}")
    public String nuevoDesdeSolicitud(@PathVariable Integer idSolicitud,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<SolicitudRecoleccion> solicitudOpt = solicitudRecoleccionService.getSolicitud(idSolicitud);

        if (solicitudOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("envio.error04", null, Locale.getDefault()));
            return "redirect:/solicitud-recoleccion/listado";
        }

        SolicitudRecoleccion solicitud = solicitudOpt.get();

        if (!"Aprobada".equals(solicitud.getEstado())) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("envio.error04", null, Locale.getDefault()));
            return "redirect:/solicitud-recoleccion/listado";
        }

        if (envioService.existeEnvioParaSolicitud(idSolicitud)) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("envio.error04", null, Locale.getDefault()));
            return "redirect:/solicitud-recoleccion/listado";
        }

        Envio envio = new Envio();
        envio.setSolicitud(solicitud);
        envio.setCliente(solicitud.getCliente());
        envio.setRepartidor(solicitud.getRepartidor());
        envio.setDireccionOrigen(solicitud.getDireccionOrigen());
        envio.setFechaRecoleccionEstimada(solicitud.getFechaHoraEstimada());
        envio.setObservacion(solicitud.getDescripcionPaquete());

        model.addAttribute("envio", envio);
        cargarListasFormulario(model);
        return "/envio/modifica";
    }

    private void cargarListasFormulario(Model model) {
        model.addAttribute("clientes", clienteService.getClientes());
        model.addAttribute("paquetes", paqueteService.getPaquetes());
        model.addAttribute("repartidores", repartidorService.getRepartidores());
        model.addAttribute("direcciones", direccionService.getDirecciones());
        model.addAttribute("estadosEnvio", estadoEnvioService.getEstadosEnvio());
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
    
    private boolean esAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
    
     private boolean esCliente(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_CLIENTE")) {
                return true;
            }
        }
        return false;
    }
     
    private boolean esRepartidor(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_REPARTIDOR")) {
                return true;
            }
        }
        return false;
    }
     
}
