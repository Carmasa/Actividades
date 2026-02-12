package com.example.plataformasaas.repositories;

import com.example.plataformasaas.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByFechaEmisionBetween(LocalDate start, LocalDate end);

    List<Factura> findByMontoGreaterThan(Double monto);
}
