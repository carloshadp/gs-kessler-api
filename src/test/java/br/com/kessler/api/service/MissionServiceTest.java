package br.com.kessler.api.service;

import br.com.kessler.api.dto.request.MissionRequest;
import br.com.kessler.api.dto.response.MissionResponse;
import br.com.kessler.api.entity.Mission;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.*;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock private MissionRepository repository;
    @Mock private OrbitalDebrisService debrisService;

    @InjectMocks private MissionService service;

    private OrbitalDebris debris;
    private Mission mission;
    private MissionRequest request;

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

        mission = new Mission();
        mission.setName("Operação Fênix I");
        mission.setDescription("Missão de remoção prioritária");
        mission.setTargetDebris(debris);
        mission.setPriority(MissionPriority.URGENT);
        mission.setStatus(MissionStatus.PLANNED);
        mission.setScheduledDate(LocalDate.of(2027, 6, 15));

        request = new MissionRequest();
        request.setName("Operação Fênix I");
        request.setDescription("Missão de remoção prioritária");
        request.setTargetDebrisId(1L);
        request.setPriority(MissionPriority.URGENT);
        request.setStatus(MissionStatus.PLANNED);
        request.setScheduledDate(LocalDate.of(2027, 6, 15));
    }

    @Test
    void findAll_returnsMappedResponses() {
        when(repository.findAll()).thenReturn(List.of(mission));
        List<MissionResponse> result = service.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Operação Fênix I");
        assertThat(result.get(0).getPriority()).isEqualTo(MissionPriority.URGENT);
    }

    @Test
    void findById_whenExists_returnsResponse() {
        when(repository.findById(1L)).thenReturn(Optional.of(mission));
        MissionResponse result = service.findById(1L);
        assertThat(result.getName()).isEqualTo("Operação Fênix I");
        assertThat(result.getStatus()).isEqualTo(MissionStatus.PLANNED);
    }

    @Test
    void findById_whenNotFound_throwsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_withValidRequest_savesAndReturns() {
        when(debrisService.getOrThrow(1L)).thenReturn(debris);
        when(repository.save(any())).thenReturn(mission);

        MissionResponse result = service.create(request);

        assertThat(result.getName()).isEqualTo("Operação Fênix I");
        assertThat(result.getTargetDebrisName()).isEqualTo("Fengyun-1C Debris");
        verify(repository).save(any(Mission.class));
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
    void update_completedStatus_setsCompletedAt() {
        request.setStatus(MissionStatus.COMPLETED);
        when(repository.findById(1L)).thenReturn(Optional.of(mission));
        when(debrisService.getOrThrow(1L)).thenReturn(debris);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        MissionResponse result = service.update(1L, request);

        assertThat(result.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        assertThat(mission.getCompletedAt()).isNotNull();
    }

    @Test
    void delete_whenExists_deletesSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(mission));
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
    void findByStatus_returnsFilteredMissions() {
        when(repository.findByStatus(MissionStatus.PLANNED)).thenReturn(List.of(mission));
        List<MissionResponse> result = service.findByStatus(MissionStatus.PLANNED);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(MissionStatus.PLANNED);
    }
}
