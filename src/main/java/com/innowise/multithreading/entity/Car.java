package com.innowise.multithreading.entity;

import com.innowise.multithreading.state.CarState;
import com.innowise.multithreading.state.impl.ArrivedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

public class Car implements Callable<RepairReport> {
    private static final Logger log = LogManager.getLogger(Car.class);

    private final int carId;
    private final PartType requiredPart;
    private final int requiredAmount;

    private CarState state;
    private int boxId;

    private Instant arrivedAt;
    private Instant boxAcquiredAt;
    private Instant partsAcquiredAt;

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

        Duration waitingForBox = Duration.between(arrivedAt, boxAcquiredAt);
        Duration waitingForParts = Duration.between(boxAcquiredAt, partsAcquiredAt);
        Duration total = Duration.between(arrivedAt, Instant.now());

        RepairReport report = new RepairReport(
                carId,
                requiredPart,
                requiredAmount,
                waitingForBox.toMillis(),
                waitingForParts.toMillis(),
                total.toMillis()
        );

        log.info("Car-{} finished processing, report: {}", carId, report);
        return report;
    }

    public void markArrived() {
        this.arrivedAt = Instant.now();
    }

    public void markBoxAcquired() {
        this.boxAcquiredAt = Instant.now();
    }

    public void markPartsAcquired() {
        this.partsAcquiredAt = Instant.now();
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
