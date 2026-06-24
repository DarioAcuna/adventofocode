package es.ulpgc.aoc2025.day05.common;

import java.util.ArrayList;
import java.util.List;

public class InventoryDatabaseParser {

    public InventoryDatabase parse(List<String> lines) {
        List<IngredientIdRange> freshRanges = new ArrayList<>();
        List<Long> availableIngredientIds = new ArrayList<>();

        boolean readingAvailableIds = false;

        for (String line : lines) {
            if (line.isBlank()) {
                readingAvailableIds = true;
                continue;
            }

            if (readingAvailableIds) {
                availableIngredientIds.add(Long.parseLong(line.trim()));
            } else {
                freshRanges.add(parseRange(line.trim()));
            }
        }

        return new InventoryDatabase(freshRanges, availableIngredientIds);
    }

    private IngredientIdRange parseRange(String line) {
        String[] bounds = line.split("-");

        if (bounds.length != 2) {
            throw new IllegalArgumentException("Invalid range: " + line);
        }

        long firstId = Long.parseLong(bounds[0]);
        long lastId = Long.parseLong(bounds[1]);

        return new IngredientIdRange(firstId, lastId);
    }
}