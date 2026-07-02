package com.innowise.multithreading.entity;

import java.util.List;
import java.util.Map;

public record InitData(
        int boxCount,
        Map<PartType, Integer> initialStock,
        Map<PartType, Integer> restockAmounts,
        int restockPeriodSeconds,
        int repairTimePerPartSeconds,
        List<CarSpecification> carSpecifications
) {
}
