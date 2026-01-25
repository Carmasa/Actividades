package com.act02.e3;

public class Main {
    public static void main(String[] args) {
        int totalAContar = 100;
        int numeroDeHilos = 5;

        System.out.println("Contando hasta " + totalAContar + " usando " + numeroDeHilos + " hilos.");

        int rango = totalAContar / numeroDeHilos;
        int resto = totalAContar % numeroDeHilos;

        int inicioActual = 1;

        for (int i = 0; i < numeroDeHilos; i++) {
            int finActual = inicioActual + rango - 1;

            // resto entre los primeros hilos
            if (resto > 0) {
                finActual++;
                resto--;
            }

            // Depurar total
            if (finActual > totalAContar)
                finActual = totalAContar;

            MultiContador trabajador = new MultiContador("Hilo-" + (i + 1), inicioActual, finActual);
            new Thread(trabajador).start();

            inicioActual = finActual + 1;
        }
    }
}
