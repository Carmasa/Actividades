package com.act02.e1;

import java.util.Random;

public class Hilo extends Thread {
    private String texto;

    public Hilo(String texto) {
        this.texto = texto;
    }

    @Override
    public void run() {
        Random aleatorio = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(texto + " - Iteración " + (i + 1));
            try {
                // sleep aleatoriamente entre 0 y 2 segundos
                Thread.sleep(aleatorio.nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
