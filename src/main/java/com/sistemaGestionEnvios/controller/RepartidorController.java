package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Repartidor;
import com.sistemaGestionEnvios.service.RepartidorService;
import com.sistemaGestionEnvios.service.UsuarioService;
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
@RequestMapping("/repartidor")
public class RepartidorController {

    private final RepartidorService repartidorService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public RepartidorController(RepartidorService repartidorService, UsuarioService usuarioService,
            MessageSource messageSource) {
        this.repartidorService = repartidorService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var repartidores = repartidorService.getRepartidores();
        model.addAttribute("repartidores", repartidores);
        model.addAttribute("totalRepartidores", repartidores.size());

        var usuarios = usuarioService.getUsuariosPorRol("REPARTIDOR");
        model.addAttribute("usuarios", usuarios);

        model.addAttribute("repartidor", new Repartidor());

        return "/repartidor/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Repartidor repartidor,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.actualizado";

        try {
            repartidorService.save(repartidor);
        } catch (Exception e) {
            titulo = "error";
            detalle = "repartidor.error04";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );

        return "redirect:/repartidor/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idRepartidor,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            repartidorService.delete(idRepartidor);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "repartidor.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "repartidor.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "repartidor.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );

        return "redirect:/repartidor/listado";
    }

    @GetMapping("/modificar/{idRepartidor}")
    public String modificar(@PathVariable("idRepartidor") Integer idRepartidor,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Repartidor> repartidorOpt = repartidorService.getRepartidor(idRepartidor);

        if (repartidorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("repartidor.error01", null, Locale.getDefault())
            );
            return "redirect:/repartidor/listado";
        }

        model.addAttribute("repartidor", repartidorOpt.get());

        var usuarios = usuarioService.getUsuariosPorRol("REPARTIDOR");
        model.addAttribute("usuarios", usuarios);

        return "/repartidor/modifica";
    }

}
