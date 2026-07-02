package com.innowise.multithreading.util;

import com.innowise.multithreading.entity.CarSpecification;
import com.innowise.multithreading.entity.InitData;
import com.innowise.multithreading.entity.PartType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class StaticResources {

    private static final AtomicReference<InitData> initializationData = new AtomicReference<>();

    private StaticResources() {
    }

    private static InitData getInitializationData() {
        InitData data = initializationData.get();
        if (data == null) {
            throw new IllegalStateException("Initialization data hasn't been set yet");
        }
        return data;
    }

    public static int getBoxCount() {
        InitData initData = getInitializationData();
        return initData.boxCount();
    }

    public static Map<PartType, Integer> getInitialStock() {
        InitData initData = getInitializationData();
        return initData.initialStock();
    }

    public static Map<PartType, Integer> getRestockAmounts() {
        InitData initData = getInitializationData();
        return initData.restockAmounts();
    }

    public static int getRestockPeriodSeconds() {
        InitData initData = getInitializationData();
        return initData.restockPeriodSeconds();
    }

    public static int getRepairTimePerPartSeconds() {
        InitData initData = getInitializationData();
        return initData.repairTimePerPartSeconds();
    }

    public static List<CarSpecification> getCarSpecifications() {
        InitData initData = getInitializationData();
        return initData.carSpecifications();
    }


    public static void setInitializationData(InitData initData) {
        boolean wasSet = initializationData.compareAndSet(null, initData);
        if (!wasSet) {
            throw new IllegalStateException("Initialization data has already been set");
        }
    }

}
