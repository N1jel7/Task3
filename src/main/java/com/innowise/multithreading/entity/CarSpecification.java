package com.innowise.multithreading.entity;

public record CarSpecification (
        PartType requiredPart,
        int requiredAmount
) {
}
