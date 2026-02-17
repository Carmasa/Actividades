package org.example.service;

import org.example.enums.EstadoReserva;
import org.example.model.Evento;
import org.example.model.Reserva;
import java.util.UUID;

public class SyncTicketService extends AbstractTicketService {

    public SyncTicketService(Evento evento) {
        super(evento);
    }

    @Override
    public synchronized boolean comprarDirecto(int cantidad) {
        if (evento.getDisponibles() >= cantidad) {
            // Simular un pequeño retardo para acentuar la necesidad de sincronización si no
            // existiera
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }

            evento.setDisponibles(evento.getDisponibles() - cantidad);
            evento.setVendidas(evento.getVendidas() + cantidad);
            return true;
        }
        return false;
    }

    @Override
    public synchronized Reserva reservar(String cliente, int cantidad, long ttlSeconds) {
        if (evento.getDisponibles() >= cantidad) {
            evento.setDisponibles(evento.getDisponibles() - cantidad);
            evento.setReservadas(evento.getReservadas() + cantidad);

            Reserva reserva = new Reserva(UUID.randomUUID().toString(), cliente, cantidad, ttlSeconds);
            reservas.add(reserva);
            return reserva;
        }
        return null;
    }

    @Override
    public synchronized boolean confirmarReserva(String reservaId) {
        for (Reserva r : reservas) {
            if (r.getId().equals(reservaId) && r.getEstado() == EstadoReserva.RESERVADA) {
                if (!r.isExpirada()) {
                    r.setEstado(EstadoReserva.CONFIRMADA);
                    evento.setReservadas(evento.getReservadas() - r.getCantidad());
                    evento.setVendidas(evento.getVendidas() + r.getCantidad());
                    reservasConfirmadas.incrementAndGet();
                    return true;
                } else {
                    // Si expiró justo antes de confirmar
                    r.setEstado(EstadoReserva.EXPIRADA);
                    evento.setReservadas(evento.getReservadas() - r.getCantidad());
                    evento.setDisponibles(evento.getDisponibles() + r.getCantidad());
                    reservasExpiradas.incrementAndGet();
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized boolean cancelarReserva(String reservaId) {
        for (Reserva r : reservas) {
            if (r.getId().equals(reservaId) && r.getEstado() == EstadoReserva.RESERVADA) {
                r.setEstado(EstadoReserva.CANCELADA);
                evento.setReservadas(evento.getReservadas() - r.getCantidad());
                evento.setDisponibles(evento.getDisponibles() + r.getCantidad());
                reservasCanceladas.incrementAndGet();
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void procesarExpiraciones() {
        for (Reserva r : reservas) {
            if (r.getEstado() == EstadoReserva.RESERVADA && r.isExpirada()) {
                r.setEstado(EstadoReserva.EXPIRADA);
                evento.setReservadas(evento.getReservadas() - r.getCantidad());
                evento.setDisponibles(evento.getDisponibles() + r.getCantidad());
                reservasExpiradas.incrementAndGet();
                System.out.println("[SISTEMA] Reserva " + r.getId() + " ha expirado.");
            }
        }
    }
}
