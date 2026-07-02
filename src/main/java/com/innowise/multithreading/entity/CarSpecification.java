package com.innowise.multithreading.entity;

public record CarSpecification(
        int carId,
        PartType requiredPart,
        int requiredAmount
) {
}
