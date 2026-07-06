package com.sistemaGestionEnvios.controller;

import com.sistemaGestionEnvios.domain.Usuario;
import com.sistemaGestionEnvios.service.RolService;
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
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService, RolService rolService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());

        var roles = rolService.getRoles();
        model.addAttribute("roles", roles);

        model.addAttribute("usuario", new Usuario());

        return "/usuario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
            RedirectAttributes redirectAttributes) {

        usuarioService.save(usuario);

        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );

        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "usuario.estadoActualizado";

        try {
            usuarioService.cambiarActivo(idUsuario);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "usuario.error01";
        } catch (Exception e) {
            titulo = "error";
            detalle = "usuario.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );

        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("usuario.error01", null, Locale.getDefault())
            );
            return "redirect:/usuario/listado";
        }

        model.addAttribute("usuario", usuarioOpt.get());

        var roles = rolService.getRoles();
        model.addAttribute("roles", roles);

        return "/usuario/modifica";
    }
}