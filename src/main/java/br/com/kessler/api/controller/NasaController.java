package br.com.kessler.api.controller;

import br.com.kessler.api.integration.NasaIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/nasa")
@RequiredArgsConstructor
@Tag(name = "NASA Integration", description = "Integração com a API da NASA DONKI (eventos solares)")
public class NasaController {

    private final NasaIntegrationService nasaService;

    @GetMapping("/cme")
    @Operation(
            summary = "Lista eventos de Ejeção de Massa Coronal (CME)",
            description = "Consulta a API pública da NASA DONKI. Eventos solares CME podem interferir " +
                    "em satélites e aumentar o risco de detritos. Padrão: últimos 30 dias."
    )
    public ResponseEntity<List<Map<String, Object>>> getCmeEvents(
            @Parameter(description = "Data início (yyyy-MM-dd)")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "Data fim (yyyy-MM-dd)")
            @RequestParam(required = false) String endDate) {

        String start = startDate != null ? startDate : LocalDate.now().minusDays(30).toString();
        String end = endDate != null ? endDate : LocalDate.now().toString();

        return ResponseEntity.ok(nasaService.getRecentCmeEvents(start, end));
    }
}
