// org/example/DAO/PersonaDAO.java
package org.example.DAO;

import org.example.entities.Persona;
import java.util.List;

public interface PersonaDAO {
    List<Persona> findAll();
    Persona findById(Integer id);
    Persona findByDNI(String dni);
    Persona create(Persona persona);
    Persona update(Persona persona);
    boolean deleteById(Integer id);
}