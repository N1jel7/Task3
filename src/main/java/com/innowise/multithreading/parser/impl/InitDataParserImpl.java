package com.innowise.multithreading.parser.impl;

import com.innowise.multithreading.entity.CarSpecification;
import com.innowise.multithreading.entity.PartType;
import com.innowise.multithreading.exception.CustomAutoException;
import com.innowise.multithreading.parser.InitData;
import com.innowise.multithreading.parser.InitDataParser;
import com.innowise.multithreading.reader.CustomTextReader;
import com.innowise.multithreading.reader.CustomTextReaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InitDataParserImpl implements InitDataParser {

    private static final Logger log = LogManager.getLogger(InitDataParserImpl.class);

    private enum Section {NONE, PARTS, RESTOCK, CARS}

    @Override
    public InitData parse(String path) throws CustomAutoException {

        CustomTextReader customTextReader = new CustomTextReaderImpl();

        List<String> lines = customTextReader.readAllLinesFromFile(path);

        int boxCount = 0;
        int restockPeriodSeconds = 0;
        int repairTimePerPart = 0;
        Map<PartType, Integer> initialStock = new EnumMap<>(PartType.class);
        Map<PartType, Integer> restockAmounts = new EnumMap<>(PartType.class);
        List<CarSpecification> carSpecifications = new ArrayList<>();

        Section currentSection = Section.NONE;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            switch (line) {
                case "PARTS" -> {
                    currentSection = Section.PARTS;
                    continue;
                }
                case "RESTOCK" -> {
                    currentSection = Section.RESTOCK;
                    continue;
                }
                case "CARS" -> {
                    currentSection = Section.CARS;
                    continue;
                }
                default -> { /* Handling below (SECTION.NONE) */ }
            }

            try {
                if (currentSection == Section.NONE) {
                    String[] p = line.split("=");
                    String key = p[0].trim();
                    String value = p[1].trim();
                    if (key.equals("BOXES")) {
                        boxCount = Integer.parseInt(value);
                    } else if (key.equals("REPAIR_TIME_PER_PART_SECONDS")) {
                        repairTimePerPart = Integer.parseInt(value);
                    } else {
                        throw new IllegalArgumentException("Unknown key outside any section: " + key);
                    }
                    continue;
                }

                if (currentSection == Section.PARTS) {
                    String[] p = line.split("=");
                    initialStock.put(PartType.valueOf(p[0].trim()), Integer.parseInt(p[1].trim()));
                    continue;
                }

                if (currentSection == Section.RESTOCK) {
                    String[] p = line.split("=");
                    String key = p[0].trim();
                    String value = p[1].trim();
                    if (key.equals("PERIOD_SECONDS")) {
                        restockPeriodSeconds = Integer.parseInt(value);
                    } else {
                        restockAmounts.put(PartType.valueOf(key), Integer.parseInt(value));
                    }
                    continue;
                }

                if (currentSection == Section.CARS) {
                    String[] p = line.split(";");
                    int carId = Integer.parseInt(p[0].trim());
                    PartType partType = PartType.valueOf(p[1].trim());
                    int amount = Integer.parseInt(p[2].trim());
                    carSpecifications.add(new CarSpecification(carId, partType, amount));
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                log.error("Failed to parse line: '{}'", line, e);
                throw new CustomAutoException("Failed to parse line: " + line, e);
            }
        }

        log.info("Init data loaded from {}: boxes={}, cars={}", path, boxCount, carSpecifications.size());

        return new InitData(boxCount, initialStock, restockAmounts,
                restockPeriodSeconds, repairTimePerPart, carSpecifications);
    }
}