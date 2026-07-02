package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.shop.CarRepairShop;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitingForBoxState implements CarState {

    private static final Logger log = LogManager.getLogger(WaitingForBoxState.class);

    @Override
    public CarState handle(Car car) throws InterruptedException {
        log.info("Car-{} is waiting for a free repair box...", car.getCarId());

        CarRepairShop autoService = CarRepairShop.getInstance();
        int boxId = autoService.getBoxPool().acquireBox();

        car.setBoxId(boxId);
        car.markBoxAcquired();
        log.info("Car-{} occupied box #{}", car.getCarId(), boxId);

        return new InBoxState();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
