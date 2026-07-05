package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Envio;
import com.sistemaGestionEnvios.service.ClienteService;
import com.sistemaGestionEnvios.service.DireccionService;
import com.sistemaGestionEnvios.service.EnvioService;
import com.sistemaGestionEnvios.service.EstadoEnvioService;
import com.sistemaGestionEnvios.service.PaqueteService;
import com.sistemaGestionEnvios.service.RepartidorService;
import com.sistemaGestionEnvios.service.RutaService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
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
    private final RutaService rutaService;
    private final DireccionService direccionService;
    private final EstadoEnvioService estadoEnvioService;
    private final MessageSource messageSource;

    public EnvioController(EnvioService envioService,
            ClienteService clienteService,
            PaqueteService paqueteService,
            RepartidorService repartidorService,
            RutaService rutaService,
            DireccionService direccionService,
            EstadoEnvioService estadoEnvioService,
            MessageSource messageSource) {
        this.envioService = envioService;
        this.clienteService = clienteService;
        this.paqueteService = paqueteService;
        this.repartidorService = repartidorService;
        this.rutaService = rutaService;
        this.direccionService = direccionService;
        this.estadoEnvioService = estadoEnvioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var envios = envioService.getEnvios();
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

    private void cargarListasFormulario(Model model) {
        model.addAttribute("clientes", clienteService.getClientes());
        model.addAttribute("paquetes", paqueteService.getPaquetes(false));
        model.addAttribute("repartidores", repartidorService.getRepartidores());
        model.addAttribute("rutas", rutaService.getRutas());
        model.addAttribute("direcciones", direccionService.getDirecciones());
        model.addAttribute("estadosEnvio", estadoEnvioService.getEstadosEnvio());
    }
}
