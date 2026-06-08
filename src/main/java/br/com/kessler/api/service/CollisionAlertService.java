package br.com.kessler.api.service;

import br.com.kessler.api.dto.request.CollisionAlertRequest;
import br.com.kessler.api.dto.response.CollisionAlertResponse;
import br.com.kessler.api.entity.CollisionAlert;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.AlertSeverity;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.repository.CollisionAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollisionAlertService {

    private final CollisionAlertRepository repository;
    private final OrbitalDebrisService debrisService;

    public List<CollisionAlertResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public CollisionAlertResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public List<CollisionAlertResponse> findActive() {
        return repository.findByAcknowledgedFalse().stream().map(this::toResponse).toList();
    }

    public List<CollisionAlertResponse> findBySeverity(AlertSeverity severity) {
        return repository.findBySeverity(severity).stream().map(this::toResponse).toList();
    }

    @Transactional
    public CollisionAlertResponse create(CollisionAlertRequest request) {
        OrbitalDebris debris = debrisService.getOrThrow(request.getPrimaryDebrisId());
        CollisionAlert alert = new CollisionAlert();
        alert.setPrimaryDebris(debris);
        alert.setSecondaryObjectId(request.getSecondaryObjectId());
        alert.setSecondaryObjectName(request.getSecondaryObjectName());
        alert.setCollisionProbability(request.getCollisionProbability());
        alert.setEstimatedTimeToEvent(request.getEstimatedTimeToEvent());
        alert.setSeverity(request.getSeverity());
        return toResponse(repository.save(alert));
    }

    @Transactional
    public CollisionAlertResponse acknowledge(Long id) {
        CollisionAlert alert = getOrThrow(id);
        alert.setAcknowledged(true);
        return toResponse(repository.save(alert));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        repository.deleteById(id);
    }

    private CollisionAlert getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta com ID " + id + " não encontrado"));
    }

    private CollisionAlertResponse toResponse(CollisionAlert a) {
        return CollisionAlertResponse.builder()
                .id(a.getId())
                .primaryDebrisId(a.getPrimaryDebris().getId())
                .primaryDebrisName(a.getPrimaryDebris().getName())
                .primaryDebrisNoradId(a.getPrimaryDebris().getNoradId())
                .secondaryObjectId(a.getSecondaryObjectId())
                .secondaryObjectName(a.getSecondaryObjectName())
                .collisionProbability(a.getCollisionProbability())
                .estimatedTimeToEvent(a.getEstimatedTimeToEvent())
                .severity(a.getSeverity())
                .acknowledged(a.getAcknowledged())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
