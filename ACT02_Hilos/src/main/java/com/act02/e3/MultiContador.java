package com.act02.e3;

public class MultiContador implements Runnable {
    private String nombre;
    private int inicio;
    private int fin;

    public MultiContador(String nombre, int inicio, int fin) {
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    public void run() {
        System.out.println(nombre + " iniciando rango [" + inicio + " - " + fin + "]");
        for (int i = inicio; i <= fin; i++) {
            System.out.println(nombre + " cuenta: " + i);
            try {
                Thread.sleep(50); // retraso para intercalar la salida
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(nombre + " finalizado.");
    }
}
