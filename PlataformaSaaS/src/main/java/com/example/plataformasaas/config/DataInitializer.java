package com.example.plataformasaas.config;

import com.example.plataformasaas.models.NivelPlan;
import com.example.plataformasaas.models.Plan;
import com.example.plataformasaas.repositories.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PlanRepository planRepository;

    @Override
    public void run(String... args) {
        if (planRepository.count() == 0) {
            planRepository.save(Plan.builder().nivel(NivelPlan.BASIC).nombre("Basic").precioMensual(10.0).build());
            planRepository.save(Plan.builder().nivel(NivelPlan.PREMIUM).nombre("Premium").precioMensual(30.0).build());
            planRepository
                    .save(Plan.builder().nivel(NivelPlan.ENTERPRISE).nombre("Enterprise").precioMensual(100.0).build());
        }
    }
}
