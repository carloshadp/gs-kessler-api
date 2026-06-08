package br.com.kessler.api.service;

import br.com.kessler.api.dto.request.OrbitalDebrisRequest;
import br.com.kessler.api.dto.response.OrbitalDebrisResponse;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.RiskLevel;
import br.com.kessler.api.exception.DuplicateResourceException;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.repository.OrbitalDebrisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrbitalDebrisService {

    private final OrbitalDebrisRepository repository;

    public List<OrbitalDebrisResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public OrbitalDebrisResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public OrbitalDebrisResponse findByNoradId(String noradId) {
        return toResponse(repository.findByNoradId(noradId)
                .orElseThrow(() -> new ResourceNotFoundException("Detrito com NORAD ID '" + noradId + "' não encontrado")));
    }

    public List<OrbitalDebrisResponse> findByRiskLevel(RiskLevel riskLevel) {
        return repository.findByRiskLevel(riskLevel).stream().map(this::toResponse).toList();
    }

    public List<OrbitalDebrisResponse> findByStatus(DebrisStatus status) {
        return repository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    public List<OrbitalDebrisResponse> findActiveCritical() {
        return repository.findActiveCritical().stream().map(this::toResponse).toList();
    }

    public List<OrbitalDebrisResponse> findByAltitudeRange(Double min, Double max) {
        return repository.findByAltitudeRange(min, max).stream().map(this::toResponse).toList();
    }

    @Transactional
    public OrbitalDebrisResponse create(OrbitalDebrisRequest request) {
        if (repository.existsByNoradId(request.getNoradId())) {
            throw new DuplicateResourceException("Detrito com NORAD ID '" + request.getNoradId() + "' já cadastrado");
        }
        OrbitalDebris debris = toEntity(request);
        return toResponse(repository.save(debris));
    }

    @Transactional
    public OrbitalDebrisResponse update(Long id, OrbitalDebrisRequest request) {
        OrbitalDebris existing = getOrThrow(id);
        if (!existing.getNoradId().equals(request.getNoradId()) && repository.existsByNoradId(request.getNoradId())) {
            throw new DuplicateResourceException("NORAD ID '" + request.getNoradId() + "' já em uso");
        }
        existing.setNoradId(request.getNoradId());
        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setAltitudeKm(request.getAltitudeKm());
        existing.setInclinationDeg(request.getInclinationDeg());
        existing.setEccentricity(request.getEccentricity());
        existing.setSizeCm(request.getSizeCm());
        existing.setMassKg(request.getMassKg());
        existing.setRiskLevel(request.getRiskLevel());
        existing.setStatus(request.getStatus());
        existing.setLaunchDate(request.getLaunchDate());
        return toResponse(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        repository.deleteById(id);
    }

    public OrbitalDebris getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detrito orbital com ID " + id + " não encontrado"));
    }

    private OrbitalDebris toEntity(OrbitalDebrisRequest req) {
        OrbitalDebris d = new OrbitalDebris();
        d.setNoradId(req.getNoradId());
        d.setName(req.getName());
        d.setType(req.getType());
        d.setAltitudeKm(req.getAltitudeKm());
        d.setInclinationDeg(req.getInclinationDeg());
        d.setEccentricity(req.getEccentricity());
        d.setSizeCm(req.getSizeCm());
        d.setMassKg(req.getMassKg());
        d.setRiskLevel(req.getRiskLevel());
        d.setStatus(req.getStatus());
        d.setLaunchDate(req.getLaunchDate());
        return d;
    }

    public OrbitalDebrisResponse toResponse(OrbitalDebris d) {
        return OrbitalDebrisResponse.builder()
                .id(d.getId())
                .noradId(d.getNoradId())
                .name(d.getName())
                .type(d.getType())
                .altitudeKm(d.getAltitudeKm())
                .inclinationDeg(d.getInclinationDeg())
                .eccentricity(d.getEccentricity())
                .sizeCm(d.getSizeCm())
                .massKg(d.getMassKg())
                .riskLevel(d.getRiskLevel())
                .status(d.getStatus())
                .launchDate(d.getLaunchDate())
                .discoveredAt(d.getDiscoveredAt())
                .orbitalPeriodMinutes(d.calculateOrbitalPeriodMinutes())
                .objectCategory(d.getObjectCategory())
                .build();
    }
}
