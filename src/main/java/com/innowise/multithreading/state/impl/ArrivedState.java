package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrivedState implements CarState {

    private static final Logger log = LogManager.getLogger(ArrivedState.class);

    @Override
    public CarState handle(Car car) {
        car.markArrived();
        log.info("Car-{} arrived at the service", car.getCarId());
        return new WaitingForBoxState();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
