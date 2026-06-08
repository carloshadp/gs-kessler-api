package br.com.kessler.api.controller;

import br.com.kessler.api.dto.request.MissionRequest;
import br.com.kessler.api.dto.response.MissionResponse;
import br.com.kessler.api.enums.MissionStatus;
import br.com.kessler.api.service.MissionService;
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
@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
@Tag(name = "Missions", description = "Gerenciamento de missões de mitigação de detritos")
public class MissionController {

    private final MissionService service;

    @GetMapping
    @Operation(summary = "Lista missões", description = "Suporta filtro por status")
    public ResponseEntity<List<MissionResponse>> findAll(
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) MissionStatus status) {
        if (status != null) return ResponseEntity.ok(service.findByStatus(status));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca missão por ID")
    public ResponseEntity<MissionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/debris/{debrisId}")
    @Operation(summary = "Lista missões por detrito alvo")
    public ResponseEntity<List<MissionResponse>> findByDebris(@PathVariable Long debrisId) {
        return ResponseEntity.ok(service.findByDebrisId(debrisId));
    }

    @PostMapping
    @Operation(summary = "Cria nova missão de mitigação")
    public ResponseEntity<MissionResponse> create(@Valid @RequestBody MissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza missão")
    public ResponseEntity<MissionResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody MissionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove missão")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
