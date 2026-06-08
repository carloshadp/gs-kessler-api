package br.com.kessler.api.dto.response;

import br.com.kessler.api.enums.AlertSeverity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CollisionAlertResponse {
    private Long id;
    private Long primaryDebrisId;
    private String primaryDebrisName;
    private String primaryDebrisNoradId;
    private String secondaryObjectId;
    private String secondaryObjectName;
    private Double collisionProbability;
    private LocalDateTime estimatedTimeToEvent;
    private AlertSeverity severity;
    private Boolean acknowledged;
    private LocalDateTime createdAt;
}
