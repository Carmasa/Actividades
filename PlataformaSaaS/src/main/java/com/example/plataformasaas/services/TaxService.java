package com.example.plataformasaas.services;

import org.springframework.stereotype.Service;

@Service
public class TaxService {

    public double calcularImpuesto(String pais) {
        if (pais == null) {
            return 0.15;
        }

        return switch (pais.toUpperCase()) {
            case "ES" -> 0.21;
            case "US" -> 0.00;
            case "FR" -> 0.20;
            default -> 0.15;
        };
    }
}
