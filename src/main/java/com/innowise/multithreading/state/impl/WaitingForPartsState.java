package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.shop.CarRepairShop;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitingForPartsState implements CarState {

    private static final Logger log = LogManager.getLogger(WaitingForPartsState.class);

    @Override
    public CarState handle(Car car) throws InterruptedException {
        CarRepairShop autoService = CarRepairShop.getInstance();
        log.info("Car-{} is waiting for parts...", car.getCarId());
        autoService.getWarehouse().takeParts(car.getRequiredPart(), car.getRequiredAmount());
        car.markPartsAcquired();
        log.info("Car-{} got parts after waiting", car.getCarId());
        return new RepairingState();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
