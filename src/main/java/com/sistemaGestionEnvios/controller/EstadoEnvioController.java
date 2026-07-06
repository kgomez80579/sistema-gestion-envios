package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.service.EstadoEnvioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estado-envio")
public class EstadoEnvioController {

    private final EstadoEnvioService estadoEnvioService;

    public EstadoEnvioController(EstadoEnvioService estadoEnvioService) {
        this.estadoEnvioService = estadoEnvioService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var estados = estadoEnvioService.getEstadosEnvio();
        model.addAttribute("estados", estados);
        model.addAttribute("totalEstados", estados.size());

        return "/estado_envio/listado";
    }

}
