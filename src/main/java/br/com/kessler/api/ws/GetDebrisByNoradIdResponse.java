package br.com.kessler.api.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "GetDebrisByNoradIdResponse", namespace = "http://kessler.com.br/ws/debris")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetDebrisByNoradIdResponse {

    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private DebrisInfo debris;
}
