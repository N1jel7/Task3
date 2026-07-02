package com.innowise.multithreading.app;

import com.innowise.multithreading.entity.Car;
import com.innowise.multithreading.entity.CarSpecification;
import com.innowise.multithreading.entity.InitData;
import com.innowise.multithreading.entity.RepairReport;
import com.innowise.multithreading.exception.CustomAutoException;
import com.innowise.multithreading.parser.InitDataParser;
import com.innowise.multithreading.parser.impl.InitDataParserImpl;
import com.innowise.multithreading.shop.CarRepairShop;
import com.innowise.multithreading.shop.SupplyTask;
import com.innowise.multithreading.util.StaticResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);
    private static final String INPUT_FILE_PATH = "data/init_data.txt";

    void main(String[] args) {
        try {
            InitDataParser loader = new InitDataParserImpl();
            InitData initData = loader.parse(INPUT_FILE_PATH);
            StaticResources.setInitializationData(initData);

            CarRepairShop carRepairShop = CarRepairShop.getInstance();

            ScheduledExecutorService supplyExecutor = Executors.newSingleThreadScheduledExecutor();
            SupplyTask supplyTask = new SupplyTask(carRepairShop.getWarehouse(), StaticResources.getRestockAmounts());
            supplyExecutor.scheduleAtFixedRate(
                    supplyTask,
                    StaticResources.getRestockPeriodSeconds(),
                    StaticResources.getRestockPeriodSeconds(),
                    TimeUnit.SECONDS
            );

            List<Car> cars = new ArrayList<>();
            for (CarSpecification spec : StaticResources.getCarSpecifications()) {
                cars.add(new Car(spec));
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