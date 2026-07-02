package com.innowise.multithreading.entity;

import com.innowise.multithreading.state.CarState;
import com.innowise.multithreading.state.impl.ArrivedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;
import java.util.concurrent.Callable;

public class Car implements Callable<RepairReport> {
    private static final Logger log = LogManager.getLogger(Car.class);

    private final int carId;
    private final PartType requiredPart;
    private final int requiredAmount;

    private CarState state;
    private int boxId;

    private long arrivedAt;
    private long boxAcquiredAt;
    private long partsAcquiredAt;

    public Car(int carId, PartType requiredPart, int requiredAmount) {
        this.carId = carId;
        this.requiredPart = requiredPart;
        this.requiredAmount = requiredAmount;
        this.state = new ArrivedState();
    }

    @Override
    public RepairReport call() throws InterruptedException {
        log.info("Car-{} started processing", carId);

        while (!state.isFinal()) {
            state = state.handle(this);
        }

        long waitingForBoxMillis = boxAcquiredAt - arrivedAt;
        long waitingForPartsMillis = partsAcquiredAt - boxAcquiredAt;
        long totalMillis = System.currentTimeMillis() - arrivedAt;

        RepairReport report = new RepairReport(
                carId,
                requiredPart,
                requiredAmount,
                waitingForBoxMillis,
                waitingForPartsMillis,
                totalMillis
        );

        log.info("Car-{} finished processing, report: {}", carId, report);
        return report;
    }

    public void markArrived() {
        this.arrivedAt = System.currentTimeMillis();
    }

    public void markBoxAcquired() {
        this.boxAcquiredAt = System.currentTimeMillis();
    }

    public void markPartsAcquired() {
        this.partsAcquiredAt = System.currentTimeMillis();
    }

    public void setState(CarState state) {
        this.state = state;
    }

    public int getCarId() {
        return carId;
    }

    public PartType getRequiredPart() {
        return requiredPart;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Car car = (Car) object;
        return carId == car.carId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(carId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("carId=" + carId)
                .add("requiredPart=" + requiredPart)
                .add("requiredAmount=" + requiredAmount)
                .add("state=" + state)
                .add("boxId=" + boxId)
                .add("arrivedAt=" + arrivedAt)
                .add("boxAcquiredAt=" + boxAcquiredAt)
                .add("partsAcquiredAt=" + partsAcquiredAt)
                .toString();
    }
}
