package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Paquete;
import com.sistemaGestionEnvios.service.PaqueteService;
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
@RequestMapping("/paquete")
public class PaqueteController {

    private final PaqueteService paqueteService;
    private final MessageSource messageSource;

    public PaqueteController(PaqueteService paqueteService, MessageSource messageSource) {
        this.paqueteService = paqueteService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var paquetes = paqueteService.getPaquetes(false);
        model.addAttribute("paquetes", paquetes);
        model.addAttribute("totalPaquetes", paquetes.size());
        return "/paquete/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Paquete paquete,
            RedirectAttributes redirectAttributes) {

        paqueteService.save(paquete);

        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );

        return "redirect:/paquete/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idPaquete,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            paqueteService.delete(idPaquete);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "paquete.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "paquete.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "paquete.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );

        return "redirect:/paquete/listado";
    }

    @GetMapping("/modificar/{idPaquete}")
    public String modificar(@PathVariable("idPaquete") Integer idPaquete,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Paquete> paqueteOpt = paqueteService.getPaquete(idPaquete);

        if (paqueteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("paquete.error01", null, Locale.getDefault())
            );
            return "redirect:/paquete/listado";
        }

        model.addAttribute("paquete", paqueteOpt.get());

        return "/paquete/modifica";
    }
}
