package es.ulpgc.aoc2025.day05.common;

public record IngredientIdRange(long firstId, long lastId) {

    public IngredientIdRange {
        if (firstId < 0) {
            throw new IllegalArgumentException("First id cannot be negative");
        }

        if (lastId < 0) {
            throw new IllegalArgumentException("Last id cannot be negative");
        }

        if (firstId > lastId) {
            throw new IllegalArgumentException("First id cannot be greater than last id");
        }
    }

    public boolean contains(long ingredientId) {
        return firstId <= ingredientId && ingredientId <= lastId;
    }

    public boolean overlapsOrTouches(IngredientIdRange other) {
        return this.firstId <= other.lastId + 1
                && other.firstId <= this.lastId + 1;
    }

    public IngredientIdRange mergeWith(IngredientIdRange other) {
        if (!overlapsOrTouches(other)) {
            throw new IllegalArgumentException("Ranges do not overlap or touch");
        }

        return new IngredientIdRange(
                Math.min(this.firstId, other.firstId),
                Math.max(this.lastId, other.lastId)
        );
    }
}