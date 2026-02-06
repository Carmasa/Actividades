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

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().email("test@example.com").password("pass").build();
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    void testRegistrarYSuscripcionConFactura() {
        Suscripcion s = suscripcionService.registrarSuscripcion(usuario, NivelPlan.BASIC);

        assertNotNull(s.getId());
        assertEquals(NivelPlan.BASIC, s.getPlan().getNivel());

        List<Factura> facturas = facturaRepository.findAll();
        assertEquals(1, facturas.size());
        assertEquals(10.0, facturas.get(0).getMonto());
    }

    @Test
    void testCambioDePlanConProrrateo() {
        Suscripcion s = suscripcionService.registrarSuscripcion(usuario, NivelPlan.BASIC);

        // Simular que han pasado 15 días (quedan 15 para la factura)
        // El cálculo en el service usa LocalDate.now(), así que será sobre los 30 días
        // iniciales
        // Precio Basic: 10, Precio Premium: 30. Diferencia: 20.
        // Si quedan 30 días, la diferencia debería ser 20€.

        suscripcionService.cambiarPlan(s.getId(), NivelPlan.PREMIUM);

        List<Factura> facturas = facturaRepository.findAll();
        // Debería haber 2 facturas: la inicial (10€) y el prorrateo (~20€)
        assertEquals(2, facturas.size());

        Factura fProrrateo = facturas.get(1);
        assertTrue(fProrrateo.getMonto() > 0, "El prorrateo debe ser positivo");
        assertTrue(fProrrateo.getConcepto().contains("Prorrateo"));
    }
}
