package com.example.plataformasaas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "pagos_paypal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class PayPalPago extends Pago {
    private String emailPaypal;
}
