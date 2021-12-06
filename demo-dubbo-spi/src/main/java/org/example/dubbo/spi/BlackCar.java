package org.example.dubbo.spi;

import lombok.Data;
import org.apache.dubbo.common.URL;

@Data
public class BlackCar implements Car {
    @Override
    public String getCarName(URL url) {
        return "black";
    }
}
