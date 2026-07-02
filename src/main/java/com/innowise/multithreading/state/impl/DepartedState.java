package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartedState implements CarState {

    private static final Logger log = LogManager.getLogger(DepartedState.class);

    @Override
    public CarState handle(Car car) {
        log.info("Car-{} departed from the service", car.getCarId());
        return this;
    }

    @Override
    public boolean isFinal() {
        return true;
    }
}
