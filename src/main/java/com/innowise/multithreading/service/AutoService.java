package com.innowise.multithreading.service;

import com.innowise.multithreading.resource.RepairBoxPool;
import com.innowise.multithreading.resource.Warehouse;

public interface AutoService {

    void init(RepairBoxPool boxPool, Warehouse warehouse, int repairTimePerPartSeconds);

    RepairBoxPool getBoxPool();

    Warehouse getWarehouse();

    int getRepairTimePerPartSeconds();
}