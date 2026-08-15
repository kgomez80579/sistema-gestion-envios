package com.sistemaGestionEnvios.controller;
 
import com.sistemaGestionEnvios.service.ClienteService;
import com.sistemaGestionEnvios.service.EnvioService;
import com.sistemaGestionEnvios.service.RepartidorService;
import com.sistemaGestionEnvios.service.EstadoEnvioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
 
@Controller
public class IndexController {
 
    private final EnvioService envioService;
    private final ClienteService clienteService;
    private final RepartidorService repartidorService;
    private final EstadoEnvioService estadoEnvioService;
 
    public IndexController(
            EnvioService envioService,
            ClienteService clienteService,
            RepartidorService repartidorService,
            EstadoEnvioService estadoEnvioService) {
 
        this.envioService = envioService;
        this.clienteService = clienteService;
        this.repartidorService = repartidorService;
        this.estadoEnvioService = estadoEnvioService;
    }
 
    @GetMapping("/")
    public String cargarPaginaInicio(Model model) {
 
        var envios = envioService.getEnvios();
        var clientes = clienteService.getClientes();
        var repartidores = repartidorService.getRepartidores();
        var estados = estadoEnvioService.getEstadosEnvio();
 
        model.addAttribute("totalEnvios", envios.size());
        model.addAttribute("totalClientes", clientes.size());
        model.addAttribute("totalRepartidores", repartidores.size());
        model.addAttribute("totalEstados", estados.size());
 
        return "/index";
    }
}