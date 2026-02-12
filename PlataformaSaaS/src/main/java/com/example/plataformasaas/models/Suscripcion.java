package com.example.plataformasaas.models;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "suscripciones")
@Audited
public class Suscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    private LocalDate fechaInicio;
    private LocalDate fechaProximaFactura;

    @Enumerated(EnumType.STRING)
    private EstadoSuscripcion estado;

    @OneToMany(mappedBy = "suscripcion", cascade = CascadeType.ALL)
    private List<Factura> facturas;

    public Suscripcion() {
    }

    public Suscripcion(Long id, Usuario usuario, Plan plan, LocalDate fechaInicio, LocalDate fechaProximaFactura,
            EstadoSuscripcion estado, List<Factura> facturas) {
        this.id = id;
        this.usuario = usuario;
        this.plan = plan;
        this.fechaInicio = fechaInicio;
        this.fechaProximaFactura = fechaProximaFactura;
        this.estado = estado;
        this.facturas = facturas;
    }

    public static SuscripcionBuilder builder() {
        return new SuscripcionBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaProximaFactura() {
        return fechaProximaFactura;
    }

    public void setFechaProximaFactura(LocalDate fechaProximaFactura) {
        this.fechaProximaFactura = fechaProximaFactura;
    }

    public EstadoSuscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoSuscripcion estado) {
        this.estado = estado;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public static class SuscripcionBuilder {
        private Long id;
        private Usuario usuario;
        private Plan plan;
        private LocalDate fechaInicio;
        private LocalDate fechaProximaFactura;
        private EstadoSuscripcion estado;
        private List<Factura> facturas;

        public SuscripcionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SuscripcionBuilder usuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        public SuscripcionBuilder plan(Plan plan) {
            this.plan = plan;
            return this;
        }

        public SuscripcionBuilder fechaInicio(LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public SuscripcionBuilder fechaProximaFactura(LocalDate fechaProximaFactura) {
            this.fechaProximaFactura = fechaProximaFactura;
            return this;
        }

        public SuscripcionBuilder estado(EstadoSuscripcion estado) {
            this.estado = estado;
            return this;
        }

        public SuscripcionBuilder facturas(List<Factura> facturas) {
            this.facturas = facturas;
            return this;
        }

        public Suscripcion build() {
            return new Suscripcion(id, usuario, plan, fechaInicio, fechaProximaFactura, estado, facturas);
        }
    }
}
