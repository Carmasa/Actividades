package org.example.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Clasificacion implements Serializable {

    public enum CalificacionAlimentacion {
        CARNIVORO,
        HERVIVORO,
        OVNIVORO
    }

    public enum CalificacionTipo {
        MAMIFERO,
        REPTIL,
        AVE,
        ANFIBIO,
        PECES
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private CalificacionAlimentacion calificacionAlimentacion;
    @Enumerated(EnumType.STRING)
    private CalificacionTipo calificaciontipo;

    @ManyToMany(mappedBy = "clasificaciones", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Animal> animales = new HashSet<>();

    // constructores
    public Clasificacion(){};

    public Clasificacion(Integer id, CalificacionAlimentacion calificacionAlimentacion, CalificacionTipo calificaciontipo) {

        this.id = id;
        this.calificacionAlimentacion = calificacionAlimentacion;
        this.calificaciontipo = calificaciontipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CalificacionAlimentacion getCalificacionAlimentacion() {
        return calificacionAlimentacion;
    }

    public void setCalificacionAlimentacion(CalificacionAlimentacion calificacionAlimentacion) {
        this.calificacionAlimentacion = calificacionAlimentacion;
    }

    public CalificacionTipo getCalificaciontipo() {
        return calificaciontipo;
    }

    public void setCalificaciontipo(CalificacionTipo calificaciontipo) {
        this.calificaciontipo = calificaciontipo;
    }
}
