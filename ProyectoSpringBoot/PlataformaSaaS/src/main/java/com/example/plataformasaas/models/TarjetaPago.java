package com.example.plataformasaas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "pagos_tarjeta")
@Audited
public class TarjetaPago extends Pago {
    private String numeroTarjetaOculto;
    private String pasarela;

    public TarjetaPago() {
    }

    public TarjetaPago(Long id, LocalDate fechaPago, Double monto, Factura factura, String numeroTarjetaOculto,
            String pasarela) {
        super(id, fechaPago, monto, factura);
        this.numeroTarjetaOculto = numeroTarjetaOculto;
        this.pasarela = pasarela;
    }

    public String getNumeroTarjetaOculto() {
        return numeroTarjetaOculto;
    }

    public void setNumeroTarjetaOculto(String numeroTarjetaOculto) {
        this.numeroTarjetaOculto = numeroTarjetaOculto;
    }

    public String getPasarela() {
        return pasarela;
    }

    public void setPasarela(String pasarela) {
        this.pasarela = pasarela;
    }
}
