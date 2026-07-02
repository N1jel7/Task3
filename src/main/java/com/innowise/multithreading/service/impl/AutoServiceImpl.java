package com.innowise.multithreading.service.impl;

import com.innowise.multithreading.resource.RepairBoxPool;
import com.innowise.multithreading.resource.Warehouse;
import com.innowise.multithreading.service.AutoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoServiceImpl implements AutoService {

    private static final Logger log = LogManager.getLogger(AutoServiceImpl.class);

    private static final AutoService instance = new AutoServiceImpl();

    private RepairBoxPool boxPool;
    private Warehouse warehouse;
    private int repairTimePerPartSeconds;

    private AutoServiceImpl() {
    }

    public static AutoService getInstance() {
        return instance;
    }

    @Override
    public void init(RepairBoxPool boxPool, Warehouse warehouse, int repairTimePerPartSeconds) {
        if (this.boxPool != null || this.warehouse != null) {
            log.warn("AutoService is already initialized, ignoring repeated init() call");
            return;
        }
        this.boxPool = boxPool;
        this.warehouse = warehouse;
        this.repairTimePerPartSeconds = repairTimePerPartSeconds;
        log.info("AutoService initialized");
    }

    @Override
    public RepairBoxPool getBoxPool() {
        return boxPool;
    }

    @Override
    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public int getRepairTimePerPartSeconds() {
        return repairTimePerPartSeconds;
    }
}