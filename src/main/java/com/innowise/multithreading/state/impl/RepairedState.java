package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.service.AutoService;
import com.innowise.multithreading.service.impl.AutoServiceImpl;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RepairedState implements CarState {

    private static final Logger log = LogManager.getLogger(RepairedState.class);

    @Override
    public CarState handle(Car car) throws InterruptedException {
        AutoService autoService = AutoServiceImpl.getInstance();
        autoService.getBoxPool().releaseBox(car.getBoxId());

        log.info("Car-{} released box #{}, repair fully completed", car.getCarId(), car.getBoxId());

        return new DepartedState();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
