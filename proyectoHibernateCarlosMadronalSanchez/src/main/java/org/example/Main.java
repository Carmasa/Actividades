package org.example;

import org.example.Util.HibernateUtil;
import org.example.DAO.AnimalDAO;
import org.example.DAO.AnimalDAOImpl;
import org.example.entities.Animal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Scanner;

import static org.example.entities.Animal.EstadoAnimal.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        AnimalDAO animalDAO = new AnimalDAOImpl();
        int opcion=0;
        do {
            System.out.println("\nMENU REFUGIO DE ANIMALES ");
            System.out.println("1. Registrar nuevo animal");
            System.out.println("2. Buscar animales por especie");
            System.out.println("3. Actualizar estado de un animal");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Registrar nuevo animal");
                    System.out.println("Introduzca el nombre del animal:");
                    String nombre = sc.nextLine();
                    System.out.println("Introduzca la especie del animal:");
                    String especie = sc.nextLine();
                    System.out.println("Introduzca la edad del animal:");
                    int edad = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Introduzca una descripción del animal:");
                    String descripcion = sc.nextLine();

                    Animal animal = new Animal(nombre, especie, null, edad, descripcion,RECIEN_ABANDONADO);

                    session.beginTransaction();
                    session.persist(animal);
                    session.getTransaction().commit();
                    System.out.println("Animal registrado con éxito.");

                    break;
                case 2:
                    System.out.println("Buscar animales por especie");
                    System.out.println("Introduzca la especie a buscar:");
                    String especieBuscar = sc.nextLine();

                    List<Animal> animales = animalDAO.findByEspecie(especieBuscar);

                    if (animales.isEmpty()) {
                        System.out.println("No se encontraron animales de la especie: " + especieBuscar);
                    } else {
                        System.out.println("Animales encontrados de la especie " + especieBuscar + ":");
                        for (Animal a : animales) {
                            System.out.println("ID: " + a.getId() + ", Nombre: " + a.getNombre() + ", Edad: " + a.getEdad() + ", Descripción: " + a.getDescripcion());
                        }
                    }

                    break;
                case 3:
                    System.out.println("Actualizar estado de un animal");
                    System.out.println("Introduzca el ID del animal a actualizar:");
                    int idActualizar = sc.nextInt();
                    sc.nextLine();
                    Animal animalActualizar = animalDAO.findById(idActualizar);

                    if (animalActualizar == null) {
                        System.out.println("No se encontró un animal con ID: " + idActualizar);
                    } else {
                        System.out.println("Animal encontrado: " + animalActualizar.getNombre() + ", Estado actual: " + animalActualizar.getEstado());
                        System.out.println("Seleccione el nuevo estado:");
                        System.out.println("1. RECIEN_ABANDONADO");
                        System.out.println("2. TIEMPO_EN_REFUGIO");
                        System.out.println("3. PROXIMAMENTE_EN_ACOGIDA");
                        int estadoOpcion = sc.nextInt();
                        sc.nextLine();

                        switch (estadoOpcion) {
                            case 1:
                                animalActualizar.setEstado(RECIEN_ABANDONADO);
                                break;
                            case 2:
                                animalActualizar.setEstado(TIEMPO_EN_REFUGIO);
                                break;
                            case 3:
                                animalActualizar.setEstado(PROXIMAMENTE_EN_ACOGIDA);
                                break;
                            default:
                                System.out.println("Opción de estado no válida.");
                                continue;
                        }

                        animalDAO.update(animalActualizar);
                        System.out.println("Estado del animal actualizado con éxito.");
                    }

                    break;
                case 4:
                    System.out.println("Saliendo del sistema");
                    session.close();
                    sessionFactory.close();
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 4);
    }
}
