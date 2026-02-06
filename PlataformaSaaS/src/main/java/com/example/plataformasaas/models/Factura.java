package com.example.plataformasaas.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
