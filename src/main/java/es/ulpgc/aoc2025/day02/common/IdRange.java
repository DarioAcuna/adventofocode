package es.ulpgc.aoc2025.day02.common;

public record IdRange(long firstId, long lastId) {

    public IdRange {
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

    public boolean contains(long id) {
        return firstId <= id && id <= lastId;
    }
}