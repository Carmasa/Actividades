package com.example.plataformasaas.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "pagos_transferencia")
@Audited
public class TransferenciaPago extends Pago {
    private String codigoIBAN;
    private String referencia;

    public TransferenciaPago() {
    }

    public TransferenciaPago(Long id, LocalDate fechaPago, Double monto, Factura factura, String codigoIBAN,
            String referencia) {
        super(id, fechaPago, monto, factura);
        this.codigoIBAN = codigoIBAN;
        this.referencia = referencia;
    }

    public String getCodigoIBAN() {
        return codigoIBAN;
    }

    public void setCodigoIBAN(String codigoIBAN) {
        this.codigoIBAN = codigoIBAN;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
