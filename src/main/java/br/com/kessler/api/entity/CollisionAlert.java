package br.com.kessler.api.entity;

import br.com.kessler.api.enums.AlertSeverity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "collision_alerts")
public class CollisionAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_debris_id", nullable = false)
    private OrbitalDebris primaryDebris;

    @Column(nullable = false)
    private String secondaryObjectId;

    @Column(nullable = false)
    private String secondaryObjectName;

    @Column(nullable = false)
    private Double collisionProbability;

    private LocalDateTime estimatedTimeToEvent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Column(nullable = false)
    private Boolean acknowledged = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
