package com.innowise.multithreading.state.impl;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.shop.CarRepairShop;
import com.innowise.multithreading.state.CarState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class InBoxState implements CarState {

    private static final Logger log = LogManager.getLogger(InBoxState.class);

    @Override
    public CarState handle(Car car) throws InterruptedException {

        CarRepairShop autoService = CarRepairShop.getInstance();

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
        return new RepairingState();


    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
