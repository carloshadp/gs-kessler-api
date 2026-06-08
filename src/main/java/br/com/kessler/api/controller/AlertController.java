package br.com.kessler.api.controller;

import br.com.kessler.api.dto.request.CollisionAlertRequest;
import br.com.kessler.api.dto.response.CollisionAlertResponse;
import br.com.kessler.api.enums.AlertSeverity;
import br.com.kessler.api.service.CollisionAlertService;
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
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Collision Alerts", description = "Alertas de colisão entre objetos orbitais")
public class AlertController {

    private final CollisionAlertService service;

    @GetMapping
    @Operation(summary = "Lista todos os alertas", description = "Suporta filtro por severidade")
    public ResponseEntity<List<CollisionAlertResponse>> findAll(
            @Parameter(description = "Filtrar por severidade") @RequestParam(required = false) AlertSeverity severity) {
        if (severity != null) return ResponseEntity.ok(service.findBySeverity(severity));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca alerta por ID")
    public ResponseEntity<CollisionAlertResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/active")
    @Operation(summary = "Lista alertas ativos", description = "Retorna apenas alertas não reconhecidos")
    public ResponseEntity<List<CollisionAlertResponse>> findActive() {
        return ResponseEntity.ok(service.findActive());
    }

    @PostMapping
    @Operation(summary = "Registra novo alerta de colisão")
    public ResponseEntity<CollisionAlertResponse> create(@Valid @RequestBody CollisionAlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}/acknowledge")
    @Operation(summary = "Reconhece um alerta", description = "Marca o alerta como ciência registrada")
    public ResponseEntity<CollisionAlertResponse> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(service.acknowledge(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove alerta")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
