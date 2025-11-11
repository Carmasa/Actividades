package org.example.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Animal implements Serializable {
    public enum EstadoAnimal {
        RECIEN_ABANDONADO,
        TIEMPO_EN_REFUGIO,
        PROXIMAMENTE_EN_ACOGIDA
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
}
