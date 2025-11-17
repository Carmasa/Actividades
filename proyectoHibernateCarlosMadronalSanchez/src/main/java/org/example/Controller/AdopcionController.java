// org/example/controller/AdopcionController.java
package org.example.Controller;

import org.example.DAO.AnimalDAO;
import org.example.DAO.PersonaDAO;
import org.example.entities.Animal;
import org.example.entities.Persona;

import java.util.List;
import java.util.Scanner;

public class AdopcionController {

    private final AnimalDAO animalDAO;
    private final PersonaDAO personaDAO;

    public AdopcionController(AnimalDAO animalDAO, PersonaDAO personaDAO) {
        this.animalDAO = animalDAO;
        this.personaDAO = personaDAO;
    }

    public void adoptarAnimal(Scanner sc) {
        System.out.print("Buscar por (1) nombre o (2) especie? ");
        int tipo = sc.nextInt();
        sc.nextLine();

        List<Animal> candidatos;
        if (tipo == 1) {
            System.out.print("Nombre: ");
            candidatos = animalDAO.findByNombre(sc.nextLine());
        } else {
            System.out.print("Especie: ");
            candidatos = animalDAO.findByEspecie(sc.nextLine());
        }

        if (candidatos.isEmpty()) {
            System.out.println("No hay animales.");
            return;
        }

        for (Animal a : candidatos) {
            System.out.printf("ID: %d, Nombre: %s, Especie: %s%n", a.getId(), a.getNombre(), a.getEspecie());
        }

        System.out.print("ID del animal: ");
        Integer id = sc.nextInt();
        sc.nextLine();

        // ✅ Buscar el animal en la lista ya cargada
        Animal animal = candidatos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (animal == null) {
            System.out.println("ID no válido.");
            return;
        }

        // Resto del código: buscar/crear persona y asignar
        System.out.print("DNI del adoptante: ");
        String dni = sc.nextLine();
        Persona persona = personaDAO.findByDNI(dni);
        if (persona == null) {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            persona = new Persona();
            persona.setDNI(dni);
            persona.setNombre(nombre);
            persona.setEmail(email);
            persona = personaDAO.create(persona);
        }

        animal.setPersona(persona);
        animal.setEstado(Animal.EstadoAnimal.PROXIMAMENTE_EN_ACOGIDA);
        animalDAO.update(animal);
        System.out.println("✅ ¡" + animal.getNombre() + " ha sido adoptado por " + persona.getNombre() + "!");
    }
}