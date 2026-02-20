package com.example.plataformasaas.models;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "planes")
@Audited
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelPlan nivel;

    private String nombre;

    @Column(nullable = false)
    private Double precioMensual;

    public Plan() {
    }

    public Plan(Long id, NivelPlan nivel, String nombre, Double precioMensual) {
        this.id = id;
        this.nivel = nivel;
        this.nombre = nombre;
        this.precioMensual = precioMensual;
    }

    public static PlanBuilder builder() {
        return new PlanBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NivelPlan getNivel() {
        return nivel;
    }

    public void setNivel(NivelPlan nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(Double precioMensual) {
        this.precioMensual = precioMensual;
    }

    public static class PlanBuilder {
        private Long id;
        private NivelPlan nivel;
        private String nombre;
        private Double precioMensual;

        public PlanBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PlanBuilder nivel(NivelPlan nivel) {
            this.nivel = nivel;
            return this;
        }

        public PlanBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public PlanBuilder precioMensual(Double precioMensual) {
            this.precioMensual = precioMensual;
            return this;
        }

        public Plan build() {
            return new Plan(id, nivel, nombre, precioMensual);
        }
    }
}
