package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Cliente;
import com.sistemaGestionEnvios.service.ClienteService;
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
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public ClienteController(ClienteService clienteService,
            UsuarioService usuarioService,
            MessageSource messageSource) {
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var clientes = clienteService.getClientes();
        model.addAttribute("clientes", clientes);
        model.addAttribute("totalClientes", clientes.size());

        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);

        model.addAttribute("cliente", new Cliente());

        return "/cliente/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Cliente cliente,
            RedirectAttributes redirectAttributes) {

        clienteService.save(cliente);

        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );

        return "redirect:/cliente/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCliente,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            clienteService.delete(idCliente);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "cliente.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "cliente.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "cliente.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );

        return "redirect:/cliente/listado";
    }

    @GetMapping("/modificar/{idCliente}")
    public String modificar(@PathVariable("idCliente") Integer idCliente,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Cliente> clienteOpt = clienteService.getCliente(idCliente);

        if (clienteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("cliente.error01", null, Locale.getDefault())
            );
            return "redirect:/cliente/listado";
        }

        model.addAttribute("cliente", clienteOpt.get());

        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);

        return "/cliente/modifica";
    }
}