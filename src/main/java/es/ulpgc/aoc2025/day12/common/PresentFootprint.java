package es.ulpgc.aoc2025.day12.common;

import java.util.Comparator;
import java.util.List;

public record PresentFootprint(List<GridCell> cells) {

    public PresentFootprint {
        if (cells == null) {
            throw new IllegalArgumentException("Cells cannot be null");
        }

        if (cells.isEmpty()) {
            throw new IllegalArgumentException("A footprint must contain at least one cell");
        }

        cells = cells.stream()
                .sorted(Comparator
                        .comparingInt(GridCell::row)
                        .thenComparingInt(GridCell::column))
                .toList();
    }

    public int area() {
        return cells.size();
    }

    public int height() {
        return cells.stream()
                .mapToInt(GridCell::row)
                .max()
                .orElseThrow() + 1;
    }

    public int width() {
        return cells.stream()
                .mapToInt(GridCell::column)
                .max()
                .orElseThrow() + 1;
    }
}