package br.com.kessler.api.entity;

import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.DebrisType;
import br.com.kessler.api.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orbital_debris")
public class OrbitalDebris extends OrbitalObject {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebrisType type;

    @Column(nullable = false)
    private Double sizeCm;

    private Double massKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebrisStatus status;

    private LocalDate launchDate;

    // Período orbital calculado via terceira lei de Kepler (aproximação para órbita circular)
    @Override
    public double calculateOrbitalPeriodMinutes() {
        double earthRadiusKm = 6371.0;
        double mu = 398600.4418; // km³/s²
        double semiMajorAxisKm = earthRadiusKm + getAltitudeKm();
        return 2 * Math.PI * Math.sqrt(Math.pow(semiMajorAxisKm, 3) / mu) / 60.0;
    }

    @Override
    public String getObjectCategory() {
        return "ORBITAL_DEBRIS";
    }
}
