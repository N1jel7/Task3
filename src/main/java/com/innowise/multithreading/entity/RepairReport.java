package com.innowise.multithreading.entity;

public record RepairReport(
        int reportId,
        int carId,
        CarSpecification carSpecification,
        long waitingForBoxMillis,
        long waitingForPartsMillis,
        long totalMillis
) {
}
