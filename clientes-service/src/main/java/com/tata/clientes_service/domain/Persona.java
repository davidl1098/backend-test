package com.tata.clientes_service.domain;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persona", schema = "clientes", indexes = {
        @Index(name = "ix_persona_identificacion", columnList = "identificacion", unique = true) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Persona {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include  
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String genero; 

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(nullable = false, length = 160)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;
}
