package br.com.kessler.api.controller;

import br.com.kessler.api.dto.request.OrbitalDebrisRequest;
import br.com.kessler.api.dto.response.OrbitalDebrisResponse;
import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.RiskLevel;
import br.com.kessler.api.service.OrbitalDebrisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/debris")
@RequiredArgsConstructor
@Tag(name = "Orbital Debris", description = "Gerenciamento de detritos orbitais")
public class DebrisController {

    private final OrbitalDebrisService service;

    @GetMapping
    @Operation(summary = "Lista todos os detritos", description = "Suporta filtros por riskLevel, status e faixa de altitude")
    public ResponseEntity<List<OrbitalDebrisResponse>> findAll(
            @Parameter(description = "Filtrar por nível de risco") @RequestParam(required = false) RiskLevel riskLevel,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) DebrisStatus status,
            @Parameter(description = "Altitude mínima em km") @RequestParam(required = false) Double altitudeMin,
            @Parameter(description = "Altitude máxima em km") @RequestParam(required = false) Double altitudeMax) {

        if (riskLevel != null) return ResponseEntity.ok(service.findByRiskLevel(riskLevel));
        if (status != null) return ResponseEntity.ok(service.findByStatus(status));
        if (altitudeMin != null && altitudeMax != null) return ResponseEntity.ok(service.findByAltitudeRange(altitudeMin, altitudeMax));

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca detrito por ID")
    public ResponseEntity<OrbitalDebrisResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/norad/{noradId}")
    @Operation(summary = "Busca detrito por NORAD ID")
    public ResponseEntity<OrbitalDebrisResponse> findByNoradId(@PathVariable String noradId) {
        return ResponseEntity.ok(service.findByNoradId(noradId));
    }

    @GetMapping("/critical")
    @Operation(summary = "Lista detritos críticos ativos", description = "Retorna detritos com riskLevel=CRITICAL e status=ACTIVE")
    public ResponseEntity<List<OrbitalDebrisResponse>> findCritical() {
        return ResponseEntity.ok(service.findActiveCritical());
    }

    @PostMapping
    @Operation(summary = "Cadastra novo detrito orbital")
    public ResponseEntity<OrbitalDebrisResponse> create(@Valid @RequestBody OrbitalDebrisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza detrito orbital")
    public ResponseEntity<OrbitalDebrisResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody OrbitalDebrisRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove detrito orbital")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
