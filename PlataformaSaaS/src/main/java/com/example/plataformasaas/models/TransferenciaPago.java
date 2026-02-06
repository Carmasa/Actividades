package com.example.plataformasaas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "pagos_transferencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TransferenciaPago extends Pago {
    private String codigoIBAN;
    private String referencia;
}
