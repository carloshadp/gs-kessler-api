package br.com.kessler.api.service;

import br.com.kessler.api.dto.request.OrbitalDebrisRequest;
import br.com.kessler.api.dto.response.OrbitalDebrisResponse;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.DebrisType;
import br.com.kessler.api.enums.RiskLevel;
import br.com.kessler.api.exception.DuplicateResourceException;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.repository.OrbitalDebrisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrbitalDebrisServiceTest {

    @Mock
    private OrbitalDebrisRepository repository;

    @InjectMocks
    private OrbitalDebrisService service;

    private OrbitalDebris debris;
    private OrbitalDebrisRequest request;

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

        request = new OrbitalDebrisRequest();
        request.setNoradId("25730");
        request.setName("Fengyun-1C Debris");
        request.setType(DebrisType.DEBRIS);
        request.setAltitudeKm(855.0);
        request.setInclinationDeg(98.8);
        request.setEccentricity(0.0013);
        request.setSizeCm(10.0);
        request.setRiskLevel(RiskLevel.CRITICAL);
        request.setStatus(DebrisStatus.ACTIVE);
    }

    @Test
    void findAll_returnsAllDebris() {
        when(repository.findAll()).thenReturn(List.of(debris));
        List<OrbitalDebrisResponse> result = service.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNoradId()).isEqualTo("25730");
    }

    @Test
    void findByNoradId_whenExists_returnsResponse() {
        when(repository.findByNoradId("25730")).thenReturn(Optional.of(debris));
        OrbitalDebrisResponse result = service.findByNoradId("25730");
        assertThat(result.getName()).isEqualTo("Fengyun-1C Debris");
        assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.CRITICAL);
    }

    @Test
    void findByNoradId_whenNotFound_throwsNotFoundException() {
        when(repository.findByNoradId("99999")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByNoradId("99999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99999");
    }

    @Test
    void create_withValidRequest_savesAndReturns() {
        when(repository.existsByNoradId("25730")).thenReturn(false);
        when(repository.save(any())).thenReturn(debris);

        OrbitalDebrisResponse result = service.create(request);

        assertThat(result.getNoradId()).isEqualTo("25730");
        verify(repository).save(any(OrbitalDebris.class));
    }

    @Test
    void create_withDuplicateNoradId_throwsDuplicateException() {
        when(repository.existsByNoradId("25730")).thenReturn(true);
        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("25730");
        verify(repository, never()).save(any());
    }

    @Test
    void delete_whenExists_deletesSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(debris));
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

    @Test
    void calculateOrbitalPeriod_forLeo855km_isApprox102min() {
        double period = debris.calculateOrbitalPeriodMinutes();
        // Período esperado para LEO ~855km é ~102 minutos
        assertThat(period).isBetween(100.0, 106.0);
    }
}
