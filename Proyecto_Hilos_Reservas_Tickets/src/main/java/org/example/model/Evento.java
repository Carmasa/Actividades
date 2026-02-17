package org.example.model;

public class Evento {
    private final String id;
    private final int total;
    private int disponibles;
    private int vendidas;
    private int reservadas;

    public Evento(String id, int total) {
        this.id = id;
        this.total = total;
        this.disponibles = total;
        this.vendidas = 0;
        this.reservadas = 0;
    }

    public String getId() {
        return id;
    }

    public int getTotal() {
        return total;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getVendidas() {
        return vendidas;
    }

    public void setVendidas(int vendidas) {
        this.vendidas = vendidas;
    }

    public int getReservadas() {
        return reservadas;
    }

    public void setReservadas(int reservadas) {
        this.reservadas = reservadas;
    }

    public boolean isConsistente() {
        return (disponibles + vendidas + reservadas) == total;
    }

    @Override
    public String toString() {
        return String.format(
                "--- ESTADO DEL EVENTO ---\n" +
                        "ID: %s\n" +
                        "Total: %d\n" +
                        "Disponibles: %d\n" +
                        "Vendidas: %d\n" +
                        "Reservadas: %d\n" +
                        "Invariante (disponibles + vendidas + reservadas = total): %b",
                id, total, disponibles, vendidas, reservadas, isConsistente());
    }
}
