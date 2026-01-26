package com.act02.e2;

public class Main {
    public static void main(String[] args) {
        int totalAContar = 20;
        int puntoMedio = totalAContar / 2;

        System.out.println("Contando hasta " + totalAContar + " con 2 hilos.");

        ContadorHilo contador1 = new ContadorHilo("Hilo-1", 1, puntoMedio);
        ContadorHilo contador2 = new ContadorHilo("Hilo-2", puntoMedio + 1, totalAContar);

        Thread h1 = new Thread(contador1);
        Thread h2 = new Thread(contador2);

        h1.start();
        h2.start();
    }
}
