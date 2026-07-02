package com.innowise.multithreading.resource;

import com.innowise.multithreading.entity.PartType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Warehouse {

    private static final Logger log = LogManager.getLogger(Warehouse.class);

    private final Map<PartType, Semaphore> stock;

    public Warehouse(Map<PartType, Integer> initialStock) {
        this.stock = new EnumMap<>(PartType.class);
        for (Map.Entry<PartType, Integer> entry : initialStock.entrySet()) {
            stock.put(entry.getKey(), new Semaphore(entry.getValue(), true));
        }
        log.info("Warehouse initialized with stock: {}", initialStock);
    }

    public boolean tryTakeParts(PartType type, int amount) {
        boolean acquired = stock.get(type).tryAcquire(amount);
        if (acquired) {
            log.info("Took {} x{} from stock immediately", type, amount);
        }
        return acquired;
    }

    public void takeParts(PartType type, int amount) throws InterruptedException {
        stock.get(type).acquire(amount);
        log.info("Took {} x{} from stock after waiting", type, amount);
    }

    public void restock(PartType type, int amount) {
        stock.get(type).release(amount);
        log.info("Restocked {} x{}", type, amount);
    }
}