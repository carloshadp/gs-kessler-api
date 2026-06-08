package br.com.kessler.api.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@XmlRootElement(name = "CalculateCollisionRiskResponse", namespace = "http://kessler.com.br/ws/debris")
@XmlAccessorType(XmlAccessType.FIELD)
public class CalculateCollisionRiskResponse {

    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String noradId1;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String noradId2;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Double altitudeDifferenceKm;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Double collisionProbability;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String riskClassification;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String recommendation;
}
