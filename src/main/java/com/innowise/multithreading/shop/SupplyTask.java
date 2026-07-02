package com.innowise.multithreading.shop;

import com.innowise.multithreading.entity.PartType;
import com.innowise.multithreading.resource.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class SupplyTask implements Runnable {

    private static final Logger log = LogManager.getLogger(SupplyTask.class);

    private final Warehouse warehouse;
    private final Map<PartType, Integer> restockAmounts;

    public SupplyTask(Warehouse warehouse, Map<PartType, Integer> restockAmounts) {
        this.warehouse = warehouse;
        this.restockAmounts = restockAmounts;
    }

    @Override
    public void run() {
        log.info("Supply truck arrived, restocking warehouse...");
        for (Map.Entry<PartType, Integer> entry : restockAmounts.entrySet()) {
            warehouse.restock(entry.getKey(), entry.getValue());
        }
    }
}