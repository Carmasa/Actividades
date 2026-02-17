package com.example.plataformasaas.config;

import com.example.plataformasaas.models.NivelPlan;
import com.example.plataformasaas.models.Rol;
import com.example.plataformasaas.models.Usuario;
import com.example.plataformasaas.repositories.PlanRepository;
import com.example.plataformasaas.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PlanRepository planRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Inicializar Planes
            if (planRepository.findByNivel(NivelPlan.BASIC).isEmpty()) {
                planRepository
                        .save(new com.example.plataformasaas.models.Plan(null, NivelPlan.BASIC, "Plan Básico", 10.0));
            }
            if (planRepository.findByNivel(NivelPlan.PREMIUM).isEmpty()) {
                planRepository.save(
                        new com.example.plataformasaas.models.Plan(null, NivelPlan.PREMIUM, "Plan Premium", 30.0));
            }
            if (planRepository.findByNivel(NivelPlan.ENTERPRISE).isEmpty()) {
                planRepository.save(new com.example.plataformasaas.models.Plan(null, NivelPlan.ENTERPRISE,
                        "Plan Enterprise", 100.0));
            }

            // Inicializar Usuarios
            if (usuarioRepository.findByEmail("admin@saas.com").isEmpty()) {
                Usuario admin = Usuario.builder()
                        .email("admin@saas.com")
                        .password(passwordEncoder.encode("admin123"))
                        .rol(Rol.ADMIN)
                        .build();
                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado: admin@saas.com / admin123");
            }

            if (usuarioRepository.findByEmail("cliente@saas.com").isEmpty()) {
                Usuario cliente = Usuario.builder()
                        .email("cliente@saas.com")
                        .password(passwordEncoder.encode("cliente123"))
                        .rol(Rol.CLIENTE)
                        .build();
                usuarioRepository.save(cliente);
                System.out.println("Usuario CLIENTE creado: cliente@saas.com / cliente123");
            }
        };
    }
}
