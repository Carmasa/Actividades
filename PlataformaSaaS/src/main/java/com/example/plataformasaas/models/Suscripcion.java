package com.example.plataformasaas.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "suscripciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
