package org.example.service;

import org.example.model.Evento;
import org.example.model.Reserva;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractTicketService implements TicketService {
    protected final Evento evento;
    protected final List<Reserva> reservas = Collections.synchronizedList(new ArrayList<>());

    protected AtomicInteger reservasConfirmadas = new AtomicInteger(0);
    protected AtomicInteger reservasCanceladas = new AtomicInteger(0);
    protected AtomicInteger reservasExpiradas = new AtomicInteger(0);

    public AbstractTicketService(Evento evento) {
        this.evento = evento;
    }

    @Override
    public Evento getEvento() {
        return evento;
    }

    @Override
    public int getReservasConfirmadasCount() {
        return reservasConfirmadas.get();
    }

    @Override
    public int getReservasCanceladasCount() {
        return reservasCanceladas.get();
    }

    @Override
    public int getReservasExpiradasCount() {
        return reservasExpiradas.get();
    }

    @Override
    public List<Reserva> getReservas() {
        return new ArrayList<>(reservas);
    }
}
