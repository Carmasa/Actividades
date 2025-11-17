// org/example/DAO/ClasificacionDAO.java
package org.example.DAO;

import org.example.entities.Clasificacion;
import java.util.List;

public interface ClasificacionDAO {
    List<Clasificacion> findAll();
    Clasificacion findById(Integer id);
    Clasificacion create(Clasificacion clasificacion);
    Clasificacion update(Clasificacion clasificacion);
    void delete(Clasificacion clasificacion);
    Clasificacion findByAlimentacionAndTipo(Clasificacion.CalificacionAlimentacion alimentacion, Clasificacion.CalificacionTipo tipo
    );
}