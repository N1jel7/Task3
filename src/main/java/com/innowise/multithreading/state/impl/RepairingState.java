package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.service.AutoService;
import com.innowise.multithreading.service.impl.AutoServiceImpl;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class RepairingState implements CarState {

    private static final Logger log = LogManager.getLogger(RepairingState.class);

    public CarState handle(Car car) throws InterruptedException {

        AutoService autoService = AutoServiceImpl.getInstance();
        int repairSeconds = autoService.getRepairTimePerPartSeconds() * car.getRequiredAmount();

        log.info("Car-{} repair started, estimated {} seconds", car.getCarId(), repairSeconds);
        TimeUnit.SECONDS.sleep(repairSeconds);
        log.info("Car-{} repair finished", car.getCarId());

        return new RepairedState();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
