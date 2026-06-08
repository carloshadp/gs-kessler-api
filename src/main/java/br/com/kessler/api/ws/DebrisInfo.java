package br.com.kessler.api.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@XmlType(name = "DebrisInfo", namespace = "http://kessler.com.br/ws/debris")
@XmlAccessorType(XmlAccessType.FIELD)
public class DebrisInfo {

    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Long id;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String noradId;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String name;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String type;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Double altitudeKm;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Double inclinationDeg;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Double eccentricity;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String riskLevel;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String status;
    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private Double orbitalPeriodMinutes;
}
