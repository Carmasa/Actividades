package com.example.plataformasaas.models;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Inheritance(strategy = InheritanceType.JOINED)
@Audited
public abstract class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaPago;
    private Double monto;

    @OneToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    public Pago() {
    }

    public Pago(Long id, LocalDate fechaPago, Double monto, Factura factura) {
        this.id = id;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.factura = factura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
