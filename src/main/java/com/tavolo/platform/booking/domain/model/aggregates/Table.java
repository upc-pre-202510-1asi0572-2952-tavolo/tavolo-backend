package com.tavolo.platform.booking.domain.model.aggregates;

import com.tavolo.platform.shared.domain.model.entities.AuditableModel; // Asumiendo que usas esto
import jakarta.persistence.*;
import lombok.NoArgsConstructor; // Solo necesitamos el constructor sin args para JPA

@Entity(name = "Tables")
// Quitamos @Getter para definir los métodos manualmente
@NoArgsConstructor // Necesario para JPA
public class Table extends AuditableModel { // Asegúrate que hereda de AuditableModel si es así

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long headquarterId; // ID de la sede a la que pertenece

    @Column(nullable = false, length = 50)
    private String identifier; // Nombre o número de la mesa

    @Column(nullable = false)
    private Integer capacity;

    // --- Constructor ---
    public Table(Long headquarterId, String identifier, Integer capacity) {
        this.headquarterId = headquarterId;
        this.identifier = identifier;
        this.capacity = capacity;
    }

    // --- GETTERS EXPLÍCITOS ---

    public Long getId() {
        return id;
    }

    public Long getHeadquarterId() { // <<<--- MÉTODO EXPLÍCITO
        return headquarterId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getCapacity() {
        return capacity;
    }

    // --- Lógica de Dominio (mínima por ahora) ---
    // Puedes añadir setters si son necesarios para alguna lógica específica,
    // pero generalmente en DDD se prefiere modificar estado a través de métodos con intención clara.

}