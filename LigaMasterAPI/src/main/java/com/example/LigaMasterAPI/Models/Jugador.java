package com.example.LigaMasterAPI.Models;

import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String posicion;
    private Integer dorsal;
    private BigDecimal valorMercado;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
    private List<Contrato> contratos;

    // Constructores
    public Jugador() {}

    public Jugador(String nombre, String posicion, Integer dorsal, BigDecimal valorMercado) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.dorsal = dorsal;
        this.valorMercado = valorMercado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public Integer getDorsal() { return dorsal; }
    public void setDorsal(Integer dorsal) { this.dorsal = dorsal; }

    public BigDecimal getValorMercado() { return valorMercado; }
    public void setValorMercado(BigDecimal valorMercado) { this.valorMercado = valorMercado; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public List<Contrato> getContratos() { return contratos; }
    public void setContratos(List<Contrato> contratos) { this.contratos = contratos; }
}
