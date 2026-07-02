package com.innowise.multithreading.config;

import com.innowise.multithreading.entity.CarSpecification;
import com.innowise.multithreading.entity.PartType;

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
