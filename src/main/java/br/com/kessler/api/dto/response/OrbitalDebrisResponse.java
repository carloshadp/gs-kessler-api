package br.com.kessler.api.dto.response;

import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.DebrisType;
import br.com.kessler.api.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class OrbitalDebrisResponse {
    private Long id;
    private String noradId;
    private String name;
    private DebrisType type;
    private Double altitudeKm;
    private Double inclinationDeg;
    private Double eccentricity;
    private Double sizeCm;
    private Double massKg;
    private RiskLevel riskLevel;
    private DebrisStatus status;
    private LocalDate launchDate;
    private LocalDateTime discoveredAt;
    private Double orbitalPeriodMinutes;
    private String objectCategory;
}
