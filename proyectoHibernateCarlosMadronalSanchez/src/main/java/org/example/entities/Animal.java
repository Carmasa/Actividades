package org.example.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Animal implements Serializable {
    public enum EstadoAnimal {
        RECIEN_ABANDONADO,
        TIEMPO_EN_REFUGIO,
        CARNIVORO, PROXIMAMENTE_EN_ACOGIDA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

    private String nombre;

    private String especie;

    private Integer edad;

    private String descripcion ;

    @Enumerated(EnumType.STRING)
    private EstadoAnimal estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "animal_clasificacion", // Nombre de la tabla intermedia (de unión)
            joinColumns = @JoinColumn(name = "animal_id"), // Columna FK para esta entidad
            inverseJoinColumns = @JoinColumn(name = "clasificacion_id") // Columna FK para la entidad inversa
    )
    private Set<Clasificacion> clasificaciones = new HashSet<>();


    // constructores
    public Animal(){};

    public Animal(String nombre, String especie, Integer id, Integer edad, String descripcion, EstadoAnimal estado) {
        this.nombre = nombre;
        this.especie = especie;
        this.id = id;
        this.edad = edad;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }
    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Set<Clasificacion> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(Set<Clasificacion> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }


}
