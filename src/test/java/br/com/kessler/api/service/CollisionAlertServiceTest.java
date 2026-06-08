package br.com.kessler.api.service;

import br.com.kessler.api.dto.request.CollisionAlertRequest;
import br.com.kessler.api.dto.response.CollisionAlertResponse;
import br.com.kessler.api.entity.CollisionAlert;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.*;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.repository.CollisionAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionAlertServiceTest {

    @Mock private CollisionAlertRepository repository;
    @Mock private OrbitalDebrisService debrisService;

    @InjectMocks private CollisionAlertService service;

    private OrbitalDebris debris;
    private CollisionAlert alert;
    private CollisionAlertRequest request;

    @BeforeEach
    void setUp() {
        debris = new OrbitalDebris();
        debris.setNoradId("25730");
        debris.setName("Fengyun-1C Debris");
        debris.setType(DebrisType.DEBRIS);
        debris.setAltitudeKm(855.0);
        debris.setInclinationDeg(98.8);
        debris.setEccentricity(0.0013);
        debris.setSizeCm(10.0);
        debris.setRiskLevel(RiskLevel.CRITICAL);
        debris.setStatus(DebrisStatus.ACTIVE);

        alert = new CollisionAlert();
        alert.setPrimaryDebris(debris);
        alert.setSecondaryObjectId("22675");
        alert.setSecondaryObjectName("Cosmos 2251 Debris");
        alert.setCollisionProbability(0.0034);
        alert.setSeverity(AlertSeverity.CRITICAL);
        alert.setAcknowledged(false);
        alert.setEstimatedTimeToEvent(LocalDateTime.of(2027, 3, 15, 4, 22));

        request = new CollisionAlertRequest();
        request.setPrimaryDebrisId(1L);
        request.setSecondaryObjectId("22675");
        request.setSecondaryObjectName("Cosmos 2251 Debris");
        request.setCollisionProbability(0.0034);
        request.setSeverity(AlertSeverity.CRITICAL);
        request.setEstimatedTimeToEvent(LocalDateTime.of(2027, 3, 15, 4, 22));
    }

    @Test
    void findAll_returnsAllAlerts() {
        when(repository.findAll()).thenReturn(List.of(alert));
        List<CollisionAlertResponse> result = service.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSecondaryObjectName()).isEqualTo("Cosmos 2251 Debris");
    }

    @Test
    void findActive_returnsOnlyUnacknowledged() {
        when(repository.findByAcknowledgedFalse()).thenReturn(List.of(alert));
        List<CollisionAlertResponse> result = service.findActive();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAcknowledged()).isFalse();
    }

    @Test
    void findBySeverity_filtersBySeverity() {
        when(repository.findBySeverity(AlertSeverity.CRITICAL)).thenReturn(List.of(alert));
        List<CollisionAlertResponse> result = service.findBySeverity(AlertSeverity.CRITICAL);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeverity()).isEqualTo(AlertSeverity.CRITICAL);
    }

    @Test
    void create_withValidRequest_savesAlert() {
        when(debrisService.getOrThrow(1L)).thenReturn(debris);
        when(repository.save(any())).thenReturn(alert);

        CollisionAlertResponse result = service.create(request);

        assertThat(result.getPrimaryDebrisName()).isEqualTo("Fengyun-1C Debris");
        assertThat(result.getCollisionProbability()).isEqualTo(0.0034);
        verify(repository).save(any(CollisionAlert.class));
    }

    @Test
    void create_withNonExistentDebris_throwsNotFoundException() {
        when(debrisService.getOrThrow(1L))
                .thenThrow(new ResourceNotFoundException("Detrito não encontrado"));
        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void acknowledge_setsAcknowledgedTrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(alert));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        CollisionAlertResponse result = service.acknowledge(1L);

        assertThat(result.getAcknowledged()).isTrue();
        verify(repository).save(alert);
    }

    @Test
    void acknowledge_whenNotFound_throwsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.acknowledge(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void delete_whenExists_deletesSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(alert));
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_whenNotFound_throwsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).deleteById(any());
    }
}
