package org.example.thread;

import org.example.model.Reserva;
import org.example.service.TicketService;
import java.util.Random;

public class Cajero implements Runnable {
    private final String id;
    private final TicketService service;
    private final Random random = new Random();
    private final int numOperaciones;

    public Cajero(String id, TicketService service, int numOperaciones) {
        this.id = id;
        this.service = service;
        this.numOperaciones = numOperaciones;
    }

    @Override
    public void run() {
        for (int i = 0; i < numOperaciones; i++) {
            ejecutarOperacionAleatoria();
            try {
                // Pequeña espera entre operaciones para simular carga real
                Thread.sleep(random.nextInt(10) + 1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void ejecutarOperacionAleatoria() {
        int accion = random.nextInt(4);
        int cantidad = random.nextInt(3) + 1; // 1 a 3 entradas

        switch (accion) {
            case 0: // Compra directa
                if (service.comprarDirecto(cantidad)) {
                    // System.out.println("[CAJERO " + id + "] Compra directa exitosa: " + cantidad
                    // + " entradas.");
                }
                break;
            case 1: // Reservar
                Reserva r = service.reservar(id, cantidad, 2); // TTL de 2 segundos
                if (r != null) {
                    // System.out.println("[CAJERO " + id + "] Reserva creada: " + r.getId());

                    // Probabilidad de confirmar o cancelar después de un tiempo
                    new Thread(() -> {
                        try {
                            Thread.sleep(random.nextInt(1000) + 500); // Esperar entre 0.5 y 1.5s
                            if (random.nextBoolean()) {
                                service.confirmarReserva(r.getId());
                            } else {
                                service.cancelarReserva(r.getId());
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                }
                break;
            case 2: // Intento de confirmación aleatoria (poco común pero posible)
                // Usualmente manejado por el hilo de arriba, pero aquí para variar
                break;
            case 3: // Solo mirar stock (no hace nada)
                service.getEvento().getDisponibles();
                break;
        }
    }
}
