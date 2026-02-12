package com.example.plataformasaas.repositories;

import com.example.plataformasaas.models.EstadoSuscripcion;
import com.example.plataformasaas.models.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    List<Suscripcion> findByFechaProximaFacturaBeforeAndEstado(LocalDate fecha, EstadoSuscripcion estado);
}
