package com.tata.clientes_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;


@Entity
@Table(name = "cliente", schema = "clientes", indexes = { @Index(name = "ix_cliente_estado", columnList = "estado") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"persona", "contrasena"}) 
public class Cliente {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "persona_id", nullable = false, unique = true)
    private Persona persona; 

    @Column(nullable = false, length = 120)
    private String contrasena; 

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVE";

}
