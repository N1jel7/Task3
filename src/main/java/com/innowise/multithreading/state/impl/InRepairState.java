package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.service.AutoService;
import com.innowise.multithreading.service.impl.AutoServiceImpl;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class InRepairState implements CarState {

    private static final Logger log = LogManager.getLogger(InRepairState.class);

    @Override
    public CarState handle(Car car) throws InterruptedException {

        AutoService autoService = AutoServiceImpl.getInstance();

        boolean partsTaken = autoService.getWarehouse()
                .tryTakeParts(car.getRequiredPart(), car.getRequiredAmount());

        if (!partsTaken) {
            log.info("Car-{} needs {} x{}, but stock is not enough right now",
                    car.getCarId(), car.getRequiredPart(), car.getRequiredAmount());
            return new WaitingForPartsState();
        }

        car.markPartsAcquired();
        log.info("Car-{} got parts immediately, starting repair...", car.getCarId());

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
