package com.example.plataformasaas.repositories;

import com.example.plataformasaas.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
