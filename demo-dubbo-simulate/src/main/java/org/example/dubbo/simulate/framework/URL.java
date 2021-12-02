package org.example.dubbo.simulate.framework;

import lombok.Data;

import java.io.Serializable;

@Data
public class URL implements Serializable {
    private String protocol;
    private String hostname;
    private Integer port;

    public URL(String protocol, String hostname, Integer port) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }
}
