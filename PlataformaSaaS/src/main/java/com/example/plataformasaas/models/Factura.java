package com.example.plataformasaas.models;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@Audited
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "suscripcion_id", nullable = false)
    private Suscripcion suscripcion;

    private LocalDate fechaEmision;
    private Double monto;
    private String concepto;

    @OneToOne(mappedBy = "factura", cascade = CascadeType.ALL)
    private Pago pago;

    public Factura() {
    }

    public Factura(Long id, Suscripcion suscripcion, LocalDate fechaEmision, Double monto, String concepto, Pago pago) {
        this.id = id;
        this.suscripcion = suscripcion;
        this.fechaEmision = fechaEmision;
        this.monto = monto;
        this.concepto = concepto;
        this.pago = pago;
    }

    public static FacturaBuilder builder() {
        return new FacturaBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public static class FacturaBuilder {
        private Long id;
        private Suscripcion suscripcion;
        private LocalDate fechaEmision;
        private Double monto;
        private String concepto;
        private Pago pago;

        public FacturaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FacturaBuilder suscripcion(Suscripcion suscripcion) {
            this.suscripcion = suscripcion;
            return this;
        }

        public FacturaBuilder fechaEmision(LocalDate fechaEmision) {
            this.fechaEmision = fechaEmision;
            return this;
        }

        public FacturaBuilder monto(Double monto) {
            this.monto = monto;
            return this;
        }

        public FacturaBuilder concepto(String concepto) {
            this.concepto = concepto;
            return this;
        }

        public FacturaBuilder pago(Pago pago) {
            this.pago = pago;
            return this;
        }

        public Factura build() {
            return new Factura(id, suscripcion, fechaEmision, monto, concepto, pago);
        }
    }
}
