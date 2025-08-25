package com.tata.clientes_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente", schema = "clientes",
       indexes = {
         @Index(name = "ix_cliente_estado", columnList = "estado"),
         @Index(name = "ix_cliente_clienteid", columnList = "cliente_id", unique = true)
       })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = "contraseniaHash")
public class Cliente extends Persona {

    @Column(name = "cliente_id", nullable = false, length = 64, unique = true)
    private String clienteId;           

    @Column(name = "contrasenia_hash", nullable = false, length = 120)
    private String contraseniaHash;      

    @Column(nullable = false)
    private Boolean estado = Boolean.TRUE;  
}
