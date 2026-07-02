package com.innowise.multithreading.app;

import com.innowise.multithreading.parser.InitData;
import com.innowise.multithreading.parser.InitDataParser;
import com.innowise.multithreading.parser.impl.InitDataParserImpl;
import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.entity.CarSpecification;
import com.innowise.multithreading.entity.RepairReport;
import com.innowise.multithreading.exception.CustomAutoException;
import com.innowise.multithreading.resource.RepairBoxPool;
import com.innowise.multithreading.resource.Warehouse;
import com.innowise.multithreading.service.AutoService;
import com.innowise.multithreading.service.impl.AutoServiceImpl;
import com.innowise.multithreading.service.impl.SupplyTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    void main(String[] args) {
        try {
            InitDataParser loader = new InitDataParserImpl();
            InitData initData = loader.parse("data/init_data.txt");

            RepairBoxPool boxPool = new RepairBoxPool(initData.boxCount());
            Warehouse warehouse = new Warehouse(initData.initialStock());

            AutoService autoService = AutoServiceImpl.getInstance();
            autoService.init(boxPool, warehouse, initData.repairTimePerPartSeconds());

            ScheduledExecutorService supplyExecutor = Executors.newSingleThreadScheduledExecutor();
            SupplyTask supplyTask = new SupplyTask(warehouse, initData.restockAmounts());
            supplyExecutor.scheduleAtFixedRate(
                    supplyTask,
                    initData.restockPeriodSeconds(),
                    initData.restockPeriodSeconds(),
                    TimeUnit.SECONDS
            );

            List<Car> cars = new ArrayList<>();
            for (CarSpecification spec : initData.carSpecifications()) {
                cars.add(new Car(spec.carId(), spec.requiredPart(), spec.requiredAmount()));
            }

            ExecutorService carExecutor = Executors.newFixedThreadPool(cars.size());
            List<Future<RepairReport>> futures = carExecutor.invokeAll(cars);

            List<RepairReport> reports = new ArrayList<>();
            for (Future<RepairReport> future : futures) {
                reports.add(future.get());
            }

            carExecutor.shutdown();
            supplyExecutor.shutdown();

            log.info("===== FINAL REPORTS =====");
            for (RepairReport report : reports) {
                log.info(report);
            }

        } catch (CustomAutoException e) {
            log.error("Failed to initialize application", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Main thread was interrupted", e);
        } catch (ExecutionException e) {
            log.error("A car thread failed with an exception", e);
        }
    }
}