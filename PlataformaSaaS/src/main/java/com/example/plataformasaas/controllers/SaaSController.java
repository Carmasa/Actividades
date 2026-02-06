package com.example.plataformasaas.controllers;

import com.example.plataformasaas.models.*;
import com.example.plataformasaas.repositories.*;
import com.example.plataformasaas.services.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SaaSController {

    private final UsuarioRepository usuarioRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final SuscripcionService suscripcionService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("planes", planRepository.findAll());
        return "index";
    }

    @PostMapping("/usuarios/registrar")
    public String registrarUsuario(@RequestParam String email) {
        Usuario usuario = Usuario.builder()
                .email(email)
                .password("1234") // Dummy password
                .build();
        usuario = usuarioRepository.save(usuario);

        // Asignar plan Basic por defecto al registrarse
        suscripcionService.registrarSuscripcion(usuario, NivelPlan.BASIC);

        return "redirect:/";
    }

    @GetMapping("/suscripciones/{id}")
    public String verSuscripcion(@PathVariable Long id, Model model) {
        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe"));
        model.addAttribute("suscripcion", suscripcion);
        model.addAttribute("revisiones", List.of()); // Here we would fetch Envers revisions if needed
        return "suscripcion";
    }

    @PostMapping("/suscripciones/{id}/cambiar-plan")
    public String cambiarPlan(@PathVariable Long id, @RequestParam NivelPlan nuevoNivel) {
        suscripcionService.cambiarPlan(id, nuevoNivel);
        return "redirect:/";
    }
}
