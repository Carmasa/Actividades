package com.mycompany.fruteriajaxb;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zamar
 */
public class GestionFruteria {
    
    public void mostrarCatalogo(Fruteria fruteria) {
        System.out.println("CATALOGO COMPLETO");
        System.out.println("Frutas:");
        for (Fruta f : fruteria.getFrutas()) {
            System.out.println("- " + f.getNombre() + " (" + f.getPrecio() + " €/kg)");
        }
        System.out.println("Verduras:");
        for (Verdura v : fruteria.getVerduras()) {
            System.out.println("- " + v.getNombre() + " (" + v.getPrecio() + " €/kg)");
        }
        System.out.println();
    }

    public void mostrarFrutasTemporada(Fruteria fruteria) {
        System.out.println("FRUTAS POR TEMPORADA");
        for (Fruta f : fruteria.getFrutas()) {
            System.out.println("- " + f.getNombre() + " -> Temporada: " + f.getTemporada());
        }
        System.out.println();
    }

    public void mostrarVerdurasPorTipo(Fruteria fruteria) {
        System.out.println("VERDURAS POR TIPO");
        for (Verdura v : fruteria.getVerduras()) {
            System.out.println("- " + v.getNombre() + " -> Tipo: " + v.getTipo());
        }
        System.out.println();
    }

    public void mostrarOfertasPorEstacion(Fruteria fruteria, String estacion) {
        System.out.println("OFERTAS DE " + estacion.toUpperCase());
        boolean hayOfertas = false;
        for (Fruta f : fruteria.getFrutas()) {
            if (f.getTemporada().equalsIgnoreCase(estacion) && f.getOferta() != null) {
                System.out.println("- " + f.getNombre() + " -> " + f.getOferta());
                hayOfertas = true;
            }
        }
        if (!hayOfertas) {
            System.out.println("No hay ofertas en esta estación.");
        }
        System.out.println();
    }
}
    

