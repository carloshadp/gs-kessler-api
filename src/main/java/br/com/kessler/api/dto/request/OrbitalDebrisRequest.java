package br.com.kessler.api.dto.request;

import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.DebrisType;
import br.com.kessler.api.enums.RiskLevel;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrbitalDebrisRequest {

    @NotBlank(message = "NORAD ID é obrigatório")
    private String noradId;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Tipo é obrigatório")
    private DebrisType type;

    @NotNull(message = "Altitude é obrigatória")
    @Positive(message = "Altitude deve ser positiva")
    private Double altitudeKm;

    @NotNull(message = "Inclinação é obrigatória")
    @DecimalMin(value = "0.0") @DecimalMax(value = "180.0")
    private Double inclinationDeg;

    @NotNull(message = "Excentricidade é obrigatória")
    @DecimalMin(value = "0.0") @DecimalMax(value = "1.0")
    private Double eccentricity;

    @Positive(message = "Tamanho deve ser positivo")
    private Double sizeCm;

    @Positive(message = "Massa deve ser positiva")
    private Double massKg;

    @NotNull(message = "Nível de risco é obrigatório")
    private RiskLevel riskLevel;

    @NotNull(message = "Status é obrigatório")
    private DebrisStatus status;

    private LocalDate launchDate;
}
