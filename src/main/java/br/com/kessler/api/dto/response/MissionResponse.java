package br.com.kessler.api.dto.response;

import br.com.kessler.api.enums.MissionPriority;
import br.com.kessler.api.enums.MissionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MissionResponse {
    private Long id;
    private String name;
    private String description;
    private Long targetDebrisId;
    private String targetDebrisName;
    private String targetDebrisNoradId;
    private MissionPriority priority;
    private MissionStatus status;
    private LocalDate scheduledDate;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}
