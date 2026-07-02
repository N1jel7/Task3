package com.innowise.multithreading.entity;

import com.innowise.multithreading.state.CarState;
import com.innowise.multithreading.state.impl.ArrivedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Callable<RepairReport> {

    private static final Logger log = LogManager.getLogger(Car.class);
    private static final AtomicInteger carIdGenerator = new AtomicInteger(1);
    private static final AtomicInteger reportIdGenerator = new AtomicInteger(1);

    private final int carId;
    private final CarSpecification carSpecification;

    private CarState state;
    private int boxId;

    private Instant arrivedAt;
    private Instant boxAcquiredAt;
    private Instant partsAcquiredAt;

    public Car(CarSpecification carSpecification) {
        this.carId = carIdGenerator.getAndIncrement();
        this.carSpecification = carSpecification;
        this.state = new ArrivedState();
    }

    @Override
    public RepairReport call() {
        log.info("Car-{} started processing", carId);

        try {
            while (!state.isFinal()) {
                state = state.handle(this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Car-{} processing was interrupted", carId);
            return null;
        }

        Duration waitingForBox = Duration.between(arrivedAt, boxAcquiredAt);
        Duration waitingForParts = Duration.between(boxAcquiredAt, partsAcquiredAt);
        Duration total = Duration.between(arrivedAt, Instant.now());

        RepairReport report = new RepairReport(
                reportIdGenerator.getAndIncrement(),
                carId,
                carSpecification,
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
        return carSpecification.requiredPart();
    }

    public int getRequiredAmount() {
        return carSpecification.requiredAmount();
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }


    public boolean equalsById(Car car) {
        if (car == null) return false;

        return carId == car.carId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Car car)) return false;

        return carId == car.carId
                && boxId == car.boxId
                && (carSpecification == null ? car.carSpecification == null : carSpecification.equals(car.carSpecification))
                && (state == null ? car.state == null : state.equals(car.state))
                && (arrivedAt == null ? car.arrivedAt == null : arrivedAt.equals(car.arrivedAt))
                && (boxAcquiredAt == null ? car.boxAcquiredAt == null : boxAcquiredAt.equals(car.boxAcquiredAt))
                && (partsAcquiredAt == null ? car.partsAcquiredAt == null : partsAcquiredAt.equals(car.partsAcquiredAt));
    }

    @Override
    public int hashCode() {
        int result = carId;
        result = 31 * result + (carSpecification != null ? carSpecification.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + boxId;
        result = 31 * result + (arrivedAt != null ? arrivedAt.hashCode() : 0);
        result = 31 * result + (boxAcquiredAt != null ? boxAcquiredAt.hashCode() : 0);
        result = 31 * result + (partsAcquiredAt != null ? partsAcquiredAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("carId=" + carId)
                .add("carSpecification=" + carSpecification)
                .add("state=" + state)
                .add("boxId=" + boxId)
                .add("arrivedAt=" + arrivedAt)
                .add("boxAcquiredAt=" + boxAcquiredAt)
                .add("partsAcquiredAt=" + partsAcquiredAt)
                .toString();
    }
}
