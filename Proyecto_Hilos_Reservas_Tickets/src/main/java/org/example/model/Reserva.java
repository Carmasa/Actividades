package org.example.model;

import org.example.enums.EstadoReserva;
import java.time.LocalDateTime;

public class Reserva {
    private final String id;
    private final String cliente;
    private final int cantidad;
    private EstadoReserva estado;
    private final LocalDateTime fechaCreacion;
    private final long ttlSeconds;

    public Reserva(String id, String cliente, int cantidad, long ttlSeconds) {
        this.id = id;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.estado = EstadoReserva.RESERVADA;
        this.fechaCreacion = LocalDateTime.now();
        this.ttlSeconds = ttlSeconds;
    }

    public String getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    public boolean isExpirada() {
        return estado == EstadoReserva.RESERVADA &&
                LocalDateTime.now().isAfter(fechaCreacion.plusSeconds(ttlSeconds));
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id='" + id + '\'' +
                ", cliente='" + cliente + '\'' +
                ", cantidad=" + cantidad +
                ", estado=" + estado +
                '}';
    }
}
