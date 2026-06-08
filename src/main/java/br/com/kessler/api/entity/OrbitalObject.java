package br.com.kessler.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class OrbitalObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String noradId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double altitudeKm;

    @Column(nullable = false)
    private Double inclinationDeg;

    @Column(nullable = false)
    private Double eccentricity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime discoveredAt = LocalDateTime.now();

    public abstract double calculateOrbitalPeriodMinutes();

    public abstract String getObjectCategory();
}
