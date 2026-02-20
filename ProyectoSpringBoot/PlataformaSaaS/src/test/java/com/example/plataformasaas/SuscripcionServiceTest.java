package com.example.plataformasaas;

import com.example.plataformasaas.models.*;
import com.example.plataformasaas.repositories.*;
import com.example.plataformasaas.services.SuscripcionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SuscripcionServiceTest {

    @Autowired
    private SuscripcionService suscripcionService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private PlanRepository planRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Inicializar planes si no existen (necesario para el servicio)
        if (planRepository.findByNivel(NivelPlan.BASIC).isEmpty()) {
            Plan basic = Plan.builder().nivel(NivelPlan.BASIC).nombre("Basic").precioMensual(10.0).build();
            planRepository.save(basic);
        }
        if (planRepository.findByNivel(NivelPlan.PREMIUM).isEmpty()) {
            Plan premium = Plan.builder().nivel(NivelPlan.PREMIUM).nombre("Premium").precioMensual(30.0).build();
            planRepository.save(premium);
        }

        usuario = Usuario.builder()
                .email("test@example.com")
                .password("pass")
                .rol(Rol.CLIENTE)
                .build();
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    void testRegistrarYSuscripcionConFactura() {
        Suscripcion s = suscripcionService.registrarSuscripcion(usuario, NivelPlan.BASIC);

        assertNotNull(s.getId());
        assertEquals(NivelPlan.BASIC, s.getPlan().getNivel());

        List<Factura> facturas = facturaRepository.findAll();
        assertEquals(1, facturas.size());
        // 10.0 base + 15% tax (default) = 11.5
        assertEquals(11.5, facturas.get(0).getMonto(), 0.01);
    }

    @Test
    void testCambioDePlanConProrrateo() {
        Suscripcion s = suscripcionService.registrarSuscripcion(usuario, NivelPlan.BASIC);

        // Simulation: Basic (10) -> Premium (30). Diff: 20 for 30 days.
        // Tax 15% on 20 = 3. Total = 23.

        suscripcionService.cambiarPlan(s.getId(), NivelPlan.PREMIUM);

        List<Factura> facturas = facturaRepository.findAll();
        assertEquals(2, facturas.size());

        Factura fProrrateo = facturas.get(1);
        assertTrue(fProrrateo.getMonto() > 0, "El prorrateo debe ser positivo");
        // Check approximate value
        assertTrue(fProrrateo.getMonto() >= 23.0, "El prorrateo debería ser >= 23.0 (20 + 3 tax)");
        assertTrue(fProrrateo.getConcepto().contains("Prorrateo"));
    }
}
