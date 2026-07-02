package com.innowise.multithreading.entity;

public record RepairReport(
        int carId,
        PartType partUsed,
        int partAmount,
        long waitingForBoxMillis,
        long waitingForPartsMillis,
        long totalMillis
) {
}
