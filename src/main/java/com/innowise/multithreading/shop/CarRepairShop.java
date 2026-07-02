package com.innowise.multithreading.shop;

import com.innowise.multithreading.resource.RepairBoxPool;
import com.innowise.multithreading.resource.Warehouse;
import com.innowise.multithreading.util.StaticResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CarRepairShop {

    private static final Logger log = LogManager.getLogger(CarRepairShop.class);
    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private static CarRepairShop instance;

    private final RepairBoxPool boxPool;
    private final Warehouse warehouse;
    private final int repairTimePerPartSeconds;

    private CarRepairShop() {
        this.boxPool = new RepairBoxPool(StaticResources.getBoxCount());
        this.warehouse = new Warehouse(StaticResources.getInitialStock());
        this.repairTimePerPartSeconds = StaticResources.getRepairTimePerPartSeconds();
    }

    public static CarRepairShop getInstance() {
        if (instance == null) {
            INSTANCE_LOCK.lock();
            try {
                if (instance == null) {
                    instance = new CarRepairShop();
                    log.info("Car repair shop successfully initialized");
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }
        return instance;
    }


    public RepairBoxPool getBoxPool() {
        return boxPool;
    }


    public Warehouse getWarehouse() {
        return warehouse;
    }

    public int getRepairTimePerPartSeconds() {
        return repairTimePerPartSeconds;
    }
}