// org/example/controller/AnimalController.java
package org.example.Controller;

import org.example.DAO.AnimalDAO;
import org.example.DAO.ClasificacionDAO;
import org.example.DAO.PersonaDAO;
import org.example.entities.*;

import java.util.List;
import java.util.Scanner;

public class AnimalController {

    private final AnimalDAO animalDAO;
    private final ClasificacionDAO clasificacionDAO;
    private final PersonaDAO personaDAO;
    private final Persona refugio;

    public AnimalController(AnimalDAO animalDAO, ClasificacionDAO clasificacionDAO, PersonaDAO personaDAO, Persona refugio) {
        this.animalDAO = animalDAO;
        this.clasificacionDAO = clasificacionDAO;
        this.personaDAO = personaDAO;
        this.refugio = refugio;
    }

    public void registrarAnimal(Scanner sc) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Especie: ");
        String especie = sc.nextLine();
        System.out.print("Edad: ");
        int edad = sc.nextInt();
        sc.nextLine();
        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        // Clasificación
        System.out.println("Alimentación: 1=CARNIVORO, 2=HERVIVORO, 3=OVNIVORO");
        Clasificacion.CalificacionAlimentacion alimentacion = switch (sc.nextInt()) {
            case 2 -> Clasificacion.CalificacionAlimentacion.HERVIVORO;
            case 3 -> Clasificacion.CalificacionAlimentacion.OVNIVORO;
            default -> Clasificacion.CalificacionAlimentacion.CARNIVORO;
        };
        sc.nextLine();

        System.out.println("Tipo: 1=MAMIFERO, 2=REPTIL, 3=AVE, 4=ANFIBIO, 5=PECES");
        Clasificacion.CalificacionTipo tipo = switch (sc.nextInt()) {
            case 2 -> Clasificacion.CalificacionTipo.REPTIL;
            case 3 -> Clasificacion.CalificacionTipo.AVE;
            case 4 -> Clasificacion.CalificacionTipo.ANFIBIO;
            case 5 -> Clasificacion.CalificacionTipo.PECES;
            default -> Clasificacion.CalificacionTipo.MAMIFERO;
        };
        sc.nextLine();

        Clasificacion existente = clasificacionDAO.findByAlimentacionAndTipo(alimentacion, tipo);
        Clasificacion clasif;
        // Si ya existe una clasificación igual, reutilizarla para evitar duplicados de esa especie
        if (existente != null) {
            clasif = existente;
        } else {
            clasif = new Clasificacion();
            clasif.setCalificacionAlimentacion(alimentacion);
            clasif.setCalificaciontipo(tipo);
        }


        Animal animal = new Animal();
        animal.setNombre(nombre);
        animal.setEspecie(especie);
        animal.setEdad(edad);
        animal.setDescripcion(descripcion);
        animal.setEstado(Animal.EstadoAnimal.RECIEN_ABANDONADO);
        animal.setPersona(refugio);
        animal.getClasificaciones().add(clasif);

        animalDAO.create(animal);
        System.out.println("Animal registrado con exito.");
    }

    public void actualizarEstado(Scanner sc) {
        System.out.print("ID del animal: ");
        Integer id = sc.nextInt();
        sc.nextLine();

        Animal animal = animalDAO.findById(id);
        if (animal == null) {
            System.out.println("Animal no encontrado.");
            return;
        }

        System.out.println("Estado actual: " + animal.getEstado());
        System.out.println("Nuevos estados:");
        System.out.println("1. RECIEN_ABANDONADO");
        System.out.println("2. TIEMPO_EN_REFUGIO");
        System.out.println("3. PROXIMAMENTE_EN_ACOGIDA");
        System.out.print("Opción: ");

        Animal.EstadoAnimal nuevoEstado = switch (sc.nextInt()) {
            case 1 -> Animal.EstadoAnimal.RECIEN_ABANDONADO;
            case 2 -> Animal.EstadoAnimal.TIEMPO_EN_REFUGIO;
            case 3 -> Animal.EstadoAnimal.PROXIMAMENTE_EN_ACOGIDA;
            default -> {
                System.out.println("Estado no válido. Sin cambios.");
                yield animal.getEstado();
            }
        };
        sc.nextLine();

        if (nuevoEstado != animal.getEstado()) {
            animal.setEstado(nuevoEstado);
            animalDAO.update(animal);
            System.out.println("Estado actualizado.");
        }
    }

    public void buscarPorEspecie(Scanner sc) {
        System.out.print("Especie: ");
        String especie = sc.nextLine();
        List<Animal> animales = animalDAO.findByEspecie(especie);
        if (animales.isEmpty()) {
            System.out.println("No se encontraron animales de esta especie.");
        } else {
            for (Animal a : animales) {
                System.out.printf("ID: %d, Nombre: %s, Estado: %s, Dueño: %s%n",
                        a.getId(), a.getNombre(), a.getEstado(), a.getPersona().getNombre());
            }
        }
    }
}