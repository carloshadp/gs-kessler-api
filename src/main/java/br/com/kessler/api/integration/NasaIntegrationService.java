package br.com.kessler.api.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NasaIntegrationService {

    // API pública da NASA DONKI — não requer chave para uso básico com api_key=DEMO_KEY
    private static final String NASA_CME_URL =
            "https://api.nasa.gov/DONKI/CME?startDate={start}&endDate={end}&api_key={key}";

    @Value("${nasa.api.key:DEMO_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public NasaIntegrationService() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getRecentCmeEvents(String startDate, String endDate) {
        try {
            log.info("Consultando NASA DONKI CME: {} -> {}", startDate, endDate);
            Object response = restTemplate.getForObject(
                    NASA_CME_URL, Object.class, startDate, endDate, apiKey);
            if (response instanceof List<?> list) {
                return (List<Map<String, Object>>) list;
            }
            return List.of();
        } catch (Exception e) {
            log.error("Erro ao consultar NASA DONKI: {}", e.getMessage());
            return List.of();
        }
    }
}
