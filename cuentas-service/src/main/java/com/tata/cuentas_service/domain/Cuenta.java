package com.tata.cuentas_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cuenta", schema = "cuentas", indexes = { @Index(name = "ix_cuenta_cliente", columnList = "cliente_id"),
        @Index(name = "ix_cuenta_estado", columnList = "estado") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cuenta {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, unique = true, length = 32)
    private String numero;

    @Column(nullable = false, length = 20) // texto simple, sin enum
    private String tipo;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private Boolean estado = Boolean.TRUE;

    @Column(name = "cliente_id", nullable = false, length = 64)
    private String clienteId;
}
