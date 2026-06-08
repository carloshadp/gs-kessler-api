package br.com.kessler.api.service;

import br.com.kessler.api.dto.request.MissionRequest;
import br.com.kessler.api.dto.response.MissionResponse;
import br.com.kessler.api.entity.Mission;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.MissionStatus;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository repository;
    private final OrbitalDebrisService debrisService;

    public List<MissionResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public MissionResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public List<MissionResponse> findByStatus(MissionStatus status) {
        return repository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public List<MissionResponse> findByDebrisId(Long debrisId) {
        return repository.findByTargetDebrisId(debrisId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public MissionResponse create(MissionRequest request) {
        OrbitalDebris debris = debrisService.getOrThrow(request.getTargetDebrisId());
        Mission mission = new Mission();
        mission.setName(request.getName());
        mission.setDescription(request.getDescription());
        mission.setTargetDebris(debris);
        mission.setPriority(request.getPriority());
        mission.setStatus(request.getStatus());
        mission.setScheduledDate(request.getScheduledDate());
        return toResponse(repository.save(mission));
    }

    @Transactional
    public MissionResponse update(Long id, MissionRequest request) {
        Mission existing = getOrThrow(id);
        OrbitalDebris debris = debrisService.getOrThrow(request.getTargetDebrisId());
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setTargetDebris(debris);
        existing.setPriority(request.getPriority());
        existing.setStatus(request.getStatus());
        existing.setScheduledDate(request.getScheduledDate());
        if (request.getStatus() == MissionStatus.COMPLETED && existing.getCompletedAt() == null) {
            existing.setCompletedAt(LocalDateTime.now());
        }
        return toResponse(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        repository.deleteById(id);
    }

    private Mission getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missão com ID " + id + " não encontrada"));
    }

    private MissionResponse toResponse(Mission m) {
        return MissionResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .targetDebrisId(m.getTargetDebris().getId())
                .targetDebrisName(m.getTargetDebris().getName())
                .targetDebrisNoradId(m.getTargetDebris().getNoradId())
                .priority(m.getPriority())
                .status(m.getStatus())
                .scheduledDate(m.getScheduledDate())
                .completedAt(m.getCompletedAt())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
