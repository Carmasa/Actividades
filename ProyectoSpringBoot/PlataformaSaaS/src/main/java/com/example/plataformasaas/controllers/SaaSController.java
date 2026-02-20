package com.example.plataformasaas.controllers;

import com.example.plataformasaas.models.*;
import com.example.plataformasaas.repositories.*;
import com.example.plataformasaas.services.SuscripcionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SaaSController {

    private final UsuarioRepository usuarioRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final SuscripcionService suscripcionService;
    private final FacturaRepository facturaRepository;
    private final PasswordEncoder passwordEncoder;

    public SaaSController(UsuarioRepository usuarioRepository, SuscripcionRepository suscripcionRepository,
            PlanRepository planRepository, SuscripcionService suscripcionService,
            FacturaRepository facturaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
        this.suscripcionService = suscripcionService;
        this.facturaRepository = facturaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String email, @RequestParam String password,
            @RequestParam String pais) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return "redirect:/register?error=Email ya existe";
        }

        com.example.plataformasaas.models.Perfil perfil = new com.example.plataformasaas.models.Perfil(null, "Usuario",
                "Nuevo", pais, null);

        Usuario usuario = Usuario.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .perfil(perfil)
                .rol(Rol.CLIENTE)
                .build();

        usuario = usuarioRepository.save(usuario);
        suscripcionService.registrarSuscripcion(usuario, NivelPlan.BASIC);

        return "redirect:/login?registered=true";
    }

    @GetMapping("/")
    public String index(Model model, java.security.Principal principal) {
        if (principal != null) {
            Usuario user = usuarioRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null && user.getRol() == Rol.CLIENTE) {
                model.addAttribute("usuarios", List.of(user));
            } else {
                model.addAttribute("usuarios", usuarioRepository.findAll());
            }
        }
        model.addAttribute("planes", planRepository.findAll());
        return "index";
    }

    @GetMapping("/billing")
    public String billing(Model model,
            @RequestParam(required = false) Double minAmount) {
        List<com.example.plataformasaas.models.Factura> facturas;
        if (minAmount != null) {
            facturas = facturaRepository.findByMontoGreaterThan(minAmount);
        } else {
            facturas = facturaRepository.findAll();
        }
        model.addAttribute("facturas", facturas);
        return "billing";
    }

    @GetMapping("/suscripciones/{id}")
    public String verSuscripcion(@PathVariable Long id, Model model) {
        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe"));
        model.addAttribute("suscripcion", suscripcion);
        model.addAttribute("revisiones", List.of());
        return "suscripcion";
    }

    @PostMapping("/suscripciones/{id}/cambiar-plan")
    public String cambiarPlan(@PathVariable Long id, @RequestParam NivelPlan nuevoNivel) {
        suscripcionService.cambiarPlan(id, nuevoNivel);
        return "redirect:/";
    }

    @PostMapping("/test/renovar")
    public String forzarRenovacion() {
        suscripcionService.renovarSuscripciones();
        return "redirect:/billing";
    }

    @PostMapping("/suscripciones/{id}/cancelar")
    public String cancelarSuscripcion(@PathVariable Long id) {
        suscripcionService.cancelarSuscripcion(id);
        return "redirect:/";
    }
}
