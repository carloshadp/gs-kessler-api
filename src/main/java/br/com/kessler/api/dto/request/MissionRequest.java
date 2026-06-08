package br.com.kessler.api.dto.request;

import br.com.kessler.api.enums.MissionPriority;
import br.com.kessler.api.enums.MissionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MissionRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "ID do detrito alvo é obrigatório")
    private Long targetDebrisId;

    @NotNull(message = "Prioridade é obrigatória")
    private MissionPriority priority;

    @NotNull(message = "Status é obrigatório")
    private MissionStatus status;

    @NotNull(message = "Data agendada é obrigatória")
    private LocalDate scheduledDate;
}
