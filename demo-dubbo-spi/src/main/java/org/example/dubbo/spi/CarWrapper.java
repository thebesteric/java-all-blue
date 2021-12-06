package org.example.dubbo.spi;

import lombok.Data;
import org.apache.dubbo.common.URL;

@Data
public class CarWrapper implements Car {

    private Car car;

    public CarWrapper(Car car) {
        this.car = car;
    }

    @Override
    public String getCarName(URL url) {
        System.out.println("wrapper...");
        return car.getCarName(url);
    }
}
