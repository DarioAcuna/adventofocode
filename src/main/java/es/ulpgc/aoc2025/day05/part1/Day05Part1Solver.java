package es.ulpgc.aoc2025.day05.part1;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day05.common.FreshIngredientRanges;
import es.ulpgc.aoc2025.day05.common.InventoryDatabase;
import es.ulpgc.aoc2025.day05.common.InventoryDatabaseParser;

import java.util.List;

public class Day05Part1Solver implements PuzzleSolver {

    private final InventoryDatabaseParser parser = new InventoryDatabaseParser();

    @Override
    public long solve(List<String> lines) {
        InventoryDatabase database = parser.parse(lines);
        FreshIngredientRanges freshRanges = new FreshIngredientRanges(database.freshRanges());

        long freshIngredientIds = 0;

        for (long ingredientId : database.availableIngredientIds()) {
            if (freshRanges.contains(ingredientId)) {
                freshIngredientIds++;
            }
        }

        return freshIngredientIds;
    }
}