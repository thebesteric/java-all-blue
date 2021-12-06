package org.example.dubbo.spi;

import lombok.Data;

@Data
public class BlackPerson implements Person {

    private Car car;

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public Car getCar() {
        return car;
    }
}
