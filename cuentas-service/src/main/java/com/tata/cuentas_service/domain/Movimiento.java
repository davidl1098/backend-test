package com.tata.cuentas_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "movimiento", schema = "cuentas", indexes = @Index(name = "ix_mov_cta_fecha", columnList = "cuenta_id, fecha"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movimiento {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @Column(nullable = false)
    private OffsetDateTime fecha = OffsetDateTime.now();

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "saldo_resultante", nullable = false, precision = 18, scale = 2)
    private BigDecimal saldoResultante;
}
