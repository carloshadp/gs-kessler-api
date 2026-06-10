package br.com.kessler.api.ws;

import br.com.kessler.api.dto.response.OrbitalDebrisResponse;
import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.exception.ResourceNotFoundException;
import br.com.kessler.api.service.OrbitalDebrisService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class DebrisEndpoint {

    private static final String NAMESPACE = "http://kessler.com.br/ws/debris";

    private final OrbitalDebrisService debrisService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetDebrisByNoradIdRequest")
    @ResponsePayload
    public GetDebrisByNoradIdResponse getDebrisByNoradId(@RequestPayload GetDebrisByNoradIdRequest request) {
        OrbitalDebrisResponse dto = debrisService.findByNoradId(request.getNoradId());

        DebrisInfo info = DebrisInfo.builder()
                .id(dto.getId())
                .noradId(dto.getNoradId())
                .name(dto.getName())
                .type(dto.getType().name())
                .altitudeKm(dto.getAltitudeKm())
                .inclinationDeg(dto.getInclinationDeg())
                .eccentricity(dto.getEccentricity())
                .riskLevel(dto.getRiskLevel().name())
                .status(dto.getStatus().name())
                .orbitalPeriodMinutes(dto.getOrbitalPeriodMinutes())
                .build();

        GetDebrisByNoradIdResponse response = new GetDebrisByNoradIdResponse();
        response.setDebris(info);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "CalculateCollisionRiskRequest")
    @ResponsePayload
    public CalculateCollisionRiskResponse calculateCollisionRisk(@RequestPayload CalculateCollisionRiskRequest request) {
        OrbitalDebris d1 = debrisService.getOrThrow(findIdByNoradId(request.getNoradId1()));
        OrbitalDebris d2 = debrisService.getOrThrow(findIdByNoradId(request.getNoradId2()));

        double altDiff = Math.abs(d1.getAltitudeKm() - d2.getAltitudeKm());

        // Modelo simplificado: quanto menor a diferença de altitude e inclinação, maior o risco
        double incDiff = Math.abs(d1.getInclinationDeg() - d2.getInclinationDeg());
        double probability = calculateProbability(altDiff, incDiff);
        String risk = classifyRisk(probability);
        String recommendation = buildRecommendation(risk, altDiff);

        CalculateCollisionRiskResponse response = new CalculateCollisionRiskResponse();
        response.setNoradId1(request.getNoradId1());
        response.setNoradId2(request.getNoradId2());
        response.setAltitudeDifferenceKm(Math.round(altDiff * 100.0) / 100.0);
        response.setCollisionProbability(Math.round(probability * 1_000_000.0) / 1_000_000.0);
        response.setRiskClassification(risk);
        response.setRecommendation(recommendation);
        return response;
    }

    private Long findIdByNoradId(String noradId) {
        return debrisService.findByNoradId(noradId).getId();
    }

    private double calculateProbability(double altDiff, double incDiff) {
        // Probabilidade inversamente proporcional à diferença de altitude e inclinação
        double altFactor = Math.max(0, 1.0 - (altDiff / 500.0));
        double incFactor = Math.max(0, 1.0 - (incDiff / 90.0));
        return altFactor * incFactor * 0.05;
    }

    private String classifyRisk(double probability) {
        if (probability >= 0.01) return "CRITICAL";
        if (probability >= 0.005) return "HIGH";
        if (probability >= 0.001) return "MEDIUM";
        return "LOW";
    }

    private String buildRecommendation(String risk, double altDiff) {
        return switch (risk) {
            case "CRITICAL" -> "Risco crítico detectado (dif. altitude: " + String.format("%.1f", altDiff) +
                    " km). Manobra evasiva imediata recomendada.";
            case "HIGH" -> "Risco alto. Monitoramento contínuo e planejamento de manobra evasiva.";
            case "MEDIUM" -> "Risco moderado. Aumentar frequência de rastreamento.";
            default -> "Risco baixo. Manter monitoramento padrão.";
        };
    }
}
