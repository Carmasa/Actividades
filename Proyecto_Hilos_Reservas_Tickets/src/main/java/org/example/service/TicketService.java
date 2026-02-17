package org.example.service;

import org.example.model.Evento;
import org.example.model.Reserva;
import java.util.List;

public interface TicketService {
    boolean comprarDirecto(int cantidad);

    Reserva reservar(String cliente, int cantidad, long ttlSeconds);

    boolean confirmarReserva(String reservaId);

    boolean cancelarReserva(String reservaId);

    void procesarExpiraciones();

    Evento getEvento();

    int getReservasConfirmadasCount();

    int getReservasCanceladasCount();

    int getReservasExpiradasCount();

    List<Reserva> getReservas();
}
