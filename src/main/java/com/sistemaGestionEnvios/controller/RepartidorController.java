package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.service.RepartidorService;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/repartidor")
public class RepartidorController {

    private final RepartidorService repartidorService;
    private final MessageSource messageSource;

    public RepartidorController(RepartidorService repartidorService,
            MessageSource messageSource) {
        this.repartidorService = repartidorService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var repartidores = repartidorService.getRepartidores();

        model.addAttribute("repartidores", repartidores);
        model.addAttribute("totalRepartidores", repartidores.size());

        return "/repartidor/listado";
    }

    @PostMapping("/guardarImagenes")
    public String guardarImagenes(@RequestParam Integer idRepartidor,
            @RequestParam(required = false) MultipartFile fotoFile,
            @RequestParam(required = false) MultipartFile licenciaFile,
            RedirectAttributes redirectAttributes) {

        try {
            repartidorService.guardarImagenes(idRepartidor, fotoFile, licenciaFile);

            redirectAttributes.addFlashAttribute(
                    "todoOk",
                    messageSource.getMessage("repartidor.imagenesActualizadas", null, Locale.getDefault())
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("repartidor.errorImagen", null, Locale.getDefault())
            );
        }

        return "redirect:/repartidor/listado";
    }
}