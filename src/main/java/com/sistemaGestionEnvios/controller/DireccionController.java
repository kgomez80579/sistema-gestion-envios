package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Direccion;
import com.sistemaGestionEnvios.service.DireccionService;
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
@RequestMapping("/direccion")
public class DireccionController {

    private final DireccionService direccionService;
    private final MessageSource messageSource;

    public DireccionController(DireccionService direccionService,
            MessageSource messageSource) {
        this.direccionService = direccionService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var direcciones = direccionService.getDirecciones();
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("totalDirecciones", direcciones.size());
        model.addAttribute("direccion", new Direccion());

        return "/direccion/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Direccion direccion,
            RedirectAttributes redirectAttributes) {

        direccionService.save(direccion);

        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );

        return "redirect:/direccion/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idDireccion,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            direccionService.delete(idDireccion);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "direccion.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "direccion.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "direccion.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );

        return "redirect:/direccion/listado";
    }

    @GetMapping("/modificar/{idDireccion}")
    public String modificar(@PathVariable("idDireccion") Integer idDireccion,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Direccion> direccionOpt = direccionService.getDireccion(idDireccion);

        if (direccionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("direccion.error01", null, Locale.getDefault())
            );
            return "redirect:/direccion/listado";
        }

        model.addAttribute("direccion", direccionOpt.get());

        return "/direccion/modifica";
    }
}