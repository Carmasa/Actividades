package com.example.plataformasaas.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaxServiceTest {

    private final TaxService taxService = new TaxService();

    @Test
    void testCalcularImpuestoSpain() {
        assertEquals(0.21, taxService.calcularImpuesto("ES"));
    }

    @Test
    void testCalcularImpuestoUSA() {
        assertEquals(0.00, taxService.calcularImpuesto("US"));
    }

    @Test
    void testCalcularImpuestoFrance() {
        assertEquals(0.20, taxService.calcularImpuesto("FR"));
    }

    @Test
    void testCalcularImpuestoDefault() {
        assertEquals(0.15, taxService.calcularImpuesto("MX"));
        assertEquals(0.15, taxService.calcularImpuesto(null));
        assertEquals(0.15, taxService.calcularImpuesto(""));
    }
}
