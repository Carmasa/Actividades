package org.example;

import org.example.model.Evento;
import org.example.service.NoSyncTicketService;
import org.example.service.SyncTicketService;
import org.example.service.TicketService;
import org.example.thread.Cajero;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Clase principal que lanza la simulación de pruebas de estrés.
 * Compara una ejecución no sincronizada (con condiciones de carrera)
 * frente a una ejecución sincronizada (consistente).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SIMULACIÓN: SISTEMA DE RESERVAS DE TICKETS    ");
        System.out.println("=================================================");

        // 1. Demostración de Condición de Carrera (Sin Sincronización)
        ejecutarSimulacion(false);

        System.out.println("\n\n");

        // 2. Ejecución con Sincronización Correcta
        ejecutarSimulacion(true);
    }

    private static void ejecutarSimulacion(boolean sincronizado) {
        String tipo = sincronizado ? "SINCRONIZADA (REENTRANT LOCK / SYNC)" : "NO SINCRONIZADA (RACE CONDITION)";
        System.out.println(">>> INICIANDO PRUEBA " + tipo + " <<<");

        int totalTickets = 1000;
        Evento evento = new Evento("EVENTO-MEGADETH-2026", totalTickets);
        TicketService service = sincronizado ? new SyncTicketService(evento) : new NoSyncTicketService(evento);

        int numCajeros = 50;
        int opsPorCajero = 100;

        ExecutorService executor = Executors.newFixedThreadPool(numCajeros);

        // Hilo de control para expiraciones automáticas (Daemon)
        Thread expirador = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(500);
                    service.procesarExpiraciones();
                }
            } catch (InterruptedException e) {
                // Terminar limpiamente
            }
        });
        expirador.setDaemon(true);
        expirador.start();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numCajeros; i++) {
            executor.execute(new Cajero("CAJERO-" + (i + 1), service, opsPorCajero));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            expirador.interrupt();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();

        mostrarInformeFinal(service, endTime - startTime);
    }

    private static void mostrarInformeFinal(TicketService service, long duracion) {
        Evento e = service.getEvento();
        int suma = e.getVendidas() + e.getReservadas() + e.getDisponibles();
        boolean consistente = e.isConsistente() && e.getDisponibles() >= 0 && suma == e.getTotal();

        System.out.println("\n-------------------------------------------");
        System.out.println("            INFORME DE SIMULACIÓN          ");
        System.out.println("-------------------------------------------");
        System.out.printf("Evento ID           : %s\n", e.getId());
        System.out.printf("Duración            : %d ms\n", duracion);
        System.out.println("-------------------------------------------");
        System.out.printf("Entradas Totales    : %d\n", e.getTotal());
        System.out.printf("Entradas Vendidas   : %d\n", e.getVendidas());
        System.out.printf("Entradas Reservadas : %d\n", e.getReservadas());
        System.out.printf("Entradas Disponibles: %d\n", e.getDisponibles());
        System.out.println("-------------------------------------------");
        System.out.printf("Balance (V + R + D) : %d %s\n", suma, (suma == e.getTotal() ? "✓" : "✗"));
        System.out.println("-------------------------------------------");
        System.out.printf("Reservas Confirmadas: %d\n", service.getReservasConfirmadasCount());
        System.out.printf("Reservas Canceladas : %d\n", service.getReservasCanceladasCount());
        System.out.printf("Reservas Expiradas  : %d\n", service.getReservasExpiradasCount());
        System.out.println("-------------------------------------------");
        System.out.printf("ESTADO DEL SISTEMA  : %s\n", (consistente ? "[OK] CONSISTENTE" : "[ERROR] INCONSISTENTE"));
        System.out.println("-------------------------------------------\n");

        if (!consistente) {
            System.err.println(
                    "ALERTA: Se ha detectado una violación de la integridad de los datos por falta de sincronización.");
        }
    }
}