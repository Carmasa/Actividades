package org.example.DAO;

import java.util.List;
import org.example.entities.Animal;
/**
 * DAO (Data Access Object)
 *
 * Create / Read / Update / Delete
 */
public interface AnimalDAO {


    /**
     *
     * @return todos los empleados
     */
    List<Animal> findAll();

    //@return devuelve un animal por un id concreto
    Animal findById(Integer id);

     //@return devuelve más de un animal por nombre
    List<Animal> findByNombre(String nombre);

    List<Animal> findByEspecie(String especie);

    // Inserta un nuevo registro
    Animal create(Animal animal);

    // Actualizar
    Animal  update (Animal animal);

    /**
     *
     * @param id
     * @return borra un id concreto
     */
    boolean deleteById (Integer id);
}
