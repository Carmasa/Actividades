package com.act02.e2;

public class ContadorHilo implements Runnable {
    private String nombre;
    private int inicio;
    private int fin;

    public ContadorHilo(String nombre, int inicio, int fin) {
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    public void run() {
        for (int i = inicio; i <= fin; i++) {
            System.out.println(nombre + " cuenta: " + i);
            try {
                Thread.sleep(100); // Simular carga de trabajo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(nombre + " finalizado.");
    }
}
