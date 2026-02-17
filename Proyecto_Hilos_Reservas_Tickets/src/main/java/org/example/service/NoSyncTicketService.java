package org.example.service;

import org.example.enums.EstadoReserva;
import org.example.model.Evento;
import org.example.model.Reserva;
import java.util.UUID;

/**
 * Versión NO sincronizada para demostrar condiciones de carrera.
 */
public class NoSyncTicketService extends AbstractTicketService {

    public NoSyncTicketService(Evento evento) {
        super(evento);
    }

    @Override
    public boolean comprarDirecto(int cantidad) {
        if (evento.getDisponibles() >= cantidad) {
            // Retardo para aumentar la probabilidad de condición de carrera
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }

            int despuesDisponibles = evento.getDisponibles() - cantidad;
            int despuesVendidas = evento.getVendidas() + cantidad;

            evento.setDisponibles(despuesDisponibles);
            evento.setVendidas(despuesVendidas);
            return true;
        }
        return false;
    }

    @Override
    public Reserva reservar(String cliente, int cantidad, long ttlSeconds) {
        if (evento.getDisponibles() >= cantidad) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }

            evento.setDisponibles(evento.getDisponibles() - cantidad);
            evento.setReservadas(evento.getReservadas() + cantidad);

            Reserva reserva = new Reserva(UUID.randomUUID().toString(), cliente, cantidad, ttlSeconds);
            reservas.add(reserva);
            return reserva;
        }
        return null;
    }

    @Override
    public boolean confirmarReserva(String reservaId) {
        for (Reserva r : reservas) {
            if (r.getId().equals(reservaId) && r.getEstado() == EstadoReserva.RESERVADA) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                r.setEstado(EstadoReserva.CONFIRMADA);
                evento.setReservadas(evento.getReservadas() - r.getCantidad());
                evento.setVendidas(evento.getVendidas() + r.getCantidad());
                reservasConfirmadas.incrementAndGet();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancelarReserva(String reservaId) {
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
    public void procesarExpiraciones() {
        for (Reserva r : reservas) {
            if (r.getEstado() == EstadoReserva.RESERVADA && r.isExpirada()) {
                r.setEstado(EstadoReserva.EXPIRADA);
                evento.setReservadas(evento.getReservadas() - r.getCantidad());
                evento.setDisponibles(evento.getDisponibles() + r.getCantidad());
                reservasExpiradas.incrementAndGet();
            }
        }
    }
}
