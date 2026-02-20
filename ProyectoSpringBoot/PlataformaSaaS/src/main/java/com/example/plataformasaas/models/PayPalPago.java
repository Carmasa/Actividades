package com.example.plataformasaas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "pagos_paypal")
@Audited
public class PayPalPago extends Pago {
    private String emailPaypal;

    public PayPalPago() {
    }

    public PayPalPago(Long id, LocalDate fechaPago, Double monto, Factura factura, String emailPaypal) {
        super(id, fechaPago, monto, factura);
        this.emailPaypal = emailPaypal;
    }

    public String getEmailPaypal() {
        return emailPaypal;
    }

    public void setEmailPaypal(String emailPaypal) {
        this.emailPaypal = emailPaypal;
    }
}
