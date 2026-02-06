package com.example.plataformasaas.services;

import com.example.plataformasaas.models.*;
import com.example.plataformasaas.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class    SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final FacturaRepository facturaRepository;
    private final PlanRepository planRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository, FacturaRepository facturaRepository,
            PlanRepository planRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.facturaRepository = facturaRepository;
        this.planRepository = planRepository;
    }

    @Transactional
    public Suscripcion registrarSuscripcion(Usuario usuario, NivelPlan nivel) {
        Plan plan = planRepository.findByNivel(nivel)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        Suscripcion suscripcion = Suscripcion.builder()
                .usuario(usuario)
                .plan(plan)
                .fechaInicio(LocalDate.now())
                .fechaProximaFactura(LocalDate.now().plusDays(30))
                .estado(EstadoSuscripcion.ACTIVA)
                .build();

        suscripcion = suscripcionRepository.save(suscripcion);

        generarFactura(suscripcion, plan.getPrecioMensual(), "Primer mes de suscripción: " + plan.getNombre());

        return suscripcion;
    }

    @Transactional
    public Suscripcion cambiarPlan(Long suscripcionId, NivelPlan nuevoNivel) {
        Suscripcion suscripcion = suscripcionRepository.findById(suscripcionId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        Plan nuevoPlan = planRepository.findByNivel(nuevoNivel)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        Plan planActual = suscripcion.getPlan();

        // Si el nuevo plan es más caro, calcular prorrateo
        if (nuevoPlan.getPrecioMensual() > planActual.getPrecioMensual()) {
            double montoProrrateo = calcularProrrateo(suscripcion, nuevoPlan);
            generarFactura(suscripcion, montoProrrateo,
                    "Prorrateo por cambio selectivo a plan superior: " + nuevoPlan.getNombre());
        }

        suscripcion.setPlan(nuevoPlan);
        return suscripcionRepository.save(suscripcion);
    }

    private double calcularProrrateo(Suscripcion suscripcion, Plan nuevoPlan) {
        LocalDate hoy = LocalDate.now();
        LocalDate proximaFactura = suscripcion.getFechaProximaFactura();

        long diasRestantes = ChronoUnit.DAYS.between(hoy, proximaFactura);
        if (diasRestantes <= 0)
            return 0.0;

        double precioActualDia = suscripcion.getPlan().getPrecioMensual() / 30.0;
        double precioNuevoDia = nuevoPlan.getPrecioMensual() / 30.0;

        // La diferencia de precio por los días que quedan del ciclo actual
        double diferenciaTotal = (precioNuevoDia - precioActualDia) * diasRestantes;

        return Math.max(0, diferenciaTotal);
    }

    public void generarFactura(Suscripcion suscripcion, Double monto, String concepto) {
        Factura factura = Factura.builder()
                .suscripcion(suscripcion)
                .fechaEmision(LocalDate.now())
                .monto(monto)
                .concepto(concepto)
                .build();
        facturaRepository.save(factura);
    }
}
