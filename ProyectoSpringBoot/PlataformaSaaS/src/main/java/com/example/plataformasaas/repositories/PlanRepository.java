package com.example.plataformasaas.repositories;

import com.example.plataformasaas.models.NivelPlan;
import com.example.plataformasaas.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByNivel(NivelPlan nivel);
}
