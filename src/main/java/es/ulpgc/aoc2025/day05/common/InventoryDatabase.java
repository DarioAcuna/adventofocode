package es.ulpgc.aoc2025.day05.common;

import java.util.List;

public record InventoryDatabase(
        List<IngredientIdRange> freshRanges,
        List<Long> availableIngredientIds
) {

    public InventoryDatabase {
        if (freshRanges == null) {
            throw new IllegalArgumentException("Fresh ranges cannot be null");
        }

        if (availableIngredientIds == null) {
            throw new IllegalArgumentException("Available ingredient ids cannot be null");
        }

        freshRanges = List.copyOf(freshRanges);
        availableIngredientIds = List.copyOf(availableIngredientIds);
    }
}