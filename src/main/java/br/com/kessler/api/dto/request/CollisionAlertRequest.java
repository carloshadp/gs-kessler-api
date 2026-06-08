package br.com.kessler.api.dto.request;

import br.com.kessler.api.enums.AlertSeverity;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollisionAlertRequest {

    @NotNull(message = "ID do detrito primário é obrigatório")
    private Long primaryDebrisId;

    @NotBlank(message = "ID do objeto secundário é obrigatório")
    private String secondaryObjectId;

    @NotBlank(message = "Nome do objeto secundário é obrigatório")
    private String secondaryObjectName;

    @NotNull(message = "Probabilidade de colisão é obrigatória")
    @DecimalMin(value = "0.0") @DecimalMax(value = "1.0")
    private Double collisionProbability;

    private LocalDateTime estimatedTimeToEvent;

    @NotNull(message = "Severidade é obrigatória")
    private AlertSeverity severity;
}
