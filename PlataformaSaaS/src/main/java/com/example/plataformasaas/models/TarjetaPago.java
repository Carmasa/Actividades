package com.example.plataformasaas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "pagos_tarjeta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TarjetaPago extends Pago {
    private String numeroTarjetaOculto;
    private String pasarela;
}
