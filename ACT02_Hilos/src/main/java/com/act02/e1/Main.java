package com.act02.e1;

public class Main {
    public static void main(String[] args) {
        Hilo hilo1 = new Hilo("Hilo 1: Hola");
        Hilo hilo2 = new Hilo("Hilo 2: Mundo");

        hilo1.start();
        hilo2.start();
    }
}
