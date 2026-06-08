package br.com.kessler.api.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "GetDebrisByNoradIdRequest", namespace = "http://kessler.com.br/ws/debris")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetDebrisByNoradIdRequest {

    @XmlElement(namespace = "http://kessler.com.br/ws/debris")
    private String noradId;
}
