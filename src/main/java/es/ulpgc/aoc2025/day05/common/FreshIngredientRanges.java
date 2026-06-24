package es.ulpgc.aoc2025.day05.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record FreshIngredientRanges(List<IngredientIdRange> mergedRanges) {

    public FreshIngredientRanges(List<IngredientIdRange> mergedRanges) {
        this.mergedRanges = merge(mergedRanges);
    }

    public boolean contains(long ingredientId) {
        int left = 0;
        int right = mergedRanges.size() - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            IngredientIdRange range = mergedRanges.get(middle);

            if (range.contains(ingredientId)) {
                return true;
            }

            if (ingredientId < range.firstId()) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        return false;
    }

    // Añadido para la parte 2.
    // Devuelve cuántos IDs distintos son considerados frescos por la unión de rangos.
    public long totalFreshIngredientIds() {
        long total = 0;

        for (IngredientIdRange range : mergedRanges) {
            total += range.lastId() - range.firstId() + 1;
        }

        return total;
    }

    private List<IngredientIdRange> merge(List<IngredientIdRange> ranges) {
        if (ranges.isEmpty()) {
            return List.of();
        }

        List<IngredientIdRange> sortedRanges = ranges.stream()
                .sorted(Comparator.comparingLong(IngredientIdRange::firstId))
                .toList();

        List<IngredientIdRange> merged = new ArrayList<>();
        IngredientIdRange current = sortedRanges.getFirst();

        for (int i = 1; i < sortedRanges.size(); i++) {
            IngredientIdRange next = sortedRanges.get(i);

            if (current.overlapsOrTouches(next)) {
                current = current.mergeWith(next);
            } else {
                merged.add(current);
                current = next;
            }
        }

        merged.add(current);

        return List.copyOf(merged);
    }
}