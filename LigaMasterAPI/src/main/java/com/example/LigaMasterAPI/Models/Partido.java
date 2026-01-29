package com.example.LigaMasterAPI.Models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate fecha;
    private Integer golesLocal;
    private Integer golesVisitante;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    // Constructores
    public Partido() {}

    public Partido(LocalDate fecha, Integer golesLocal, Integer golesVisitante) {
        this.fecha = fecha;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }

    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
}

