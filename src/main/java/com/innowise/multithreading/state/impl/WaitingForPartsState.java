package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.service.AutoService;
import com.innowise.multithreading.service.impl.AutoServiceImpl;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class WaitingForPartsState implements CarState {

    private static final Logger log = LogManager.getLogger(WaitingForPartsState.class);

    @Override
    public CarState handle(Car car) throws InterruptedException {
        log.info("Car-{} is waiting for parts to be restocked...", car.getCarId());

        AutoService autoService = AutoServiceImpl.getInstance();
        autoService.getWarehouse().takeParts(car.getRequiredPart(), car.getRequiredAmount());

        car.markPartsAcquired();
        log.info("Car-{} got parts after waiting, starting repair...", car.getCarId());

        int repairSeconds = autoService.getRepairTimePerPartSeconds() * car.getRequiredAmount();
        TimeUnit.SECONDS.sleep(repairSeconds);

        log.info("Car-{} repair finished", car.getCarId());
        return new RepairedState();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
