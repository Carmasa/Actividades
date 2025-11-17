// org/example/Main.java
package org.example;

import org.example.DAO.*;
import org.example.Controller.AnimalController;
import org.example.Controller.AdopcionController;
import org.example.entities.Persona;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // DAOs
        AnimalDAO animalDAO = new AnimalDAOImpl();
        PersonaDAO personaDAO = new PersonaDAOImpl();
        ClasificacionDAO clasificacionDAO = new ClasificacionDAOImpl();

        Persona refugio = personaDAO.findById(1);
        if (refugio == null) {
            refugio = new Persona();
            refugio.setNombre("Refugio");
            refugio.setEmail("refugio@animales.com");
            refugio.setDNI("00000000R");
            refugio = personaDAO.create(refugio);
        }

        AnimalController animalController = new AnimalController(animalDAO, clasificacionDAO, personaDAO, refugio);
        AdopcionController adopcionController = new AdopcionController(animalDAO, personaDAO);

        int opcion;
        do {
            System.out.println("\nMENÚ REFUGIO DE ANIMALES");
            System.out.println("1. Registrar nuevo animal");
            System.out.println("2. Buscar animales por especie");
            System.out.println("3. Actualizar estado de un animal");
            System.out.println("4. Adoptar un animal");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> animalController.registrarAnimal(sc);
                case 2 -> animalController.buscarPorEspecie(sc);
                case 3 -> animalController.actualizarEstado(sc);
                case 4 -> adopcionController.adoptarAnimal(sc);
                case 5 -> System.out.println("Fin del programa.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
        sc.close();
    }
}