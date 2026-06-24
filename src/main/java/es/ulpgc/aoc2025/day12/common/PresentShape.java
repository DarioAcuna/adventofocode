package es.ulpgc.aoc2025.day12.common;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public record PresentShape(int index, List<String> rows) {

    public PresentShape {
        if (index < 0) {
            throw new IllegalArgumentException("Shape index cannot be negative");
        }

        if (rows == null) {
            throw new IllegalArgumentException("Rows cannot be null");
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Rows cannot be empty");
        }

        int width = rows.getFirst().length();

        for (String row : rows) {
            if (row.length() != width) {
                throw new IllegalArgumentException("All shape rows must have the same width");
            }

            if (!row.matches("[.#]+")) {
                throw new IllegalArgumentException("Shape rows can only contain '.' and '#'");
            }
        }

        rows = List.copyOf(rows);
    }

    public int area() {
        int area = 0;

        for (String row : rows) {
            for (int column = 0; column < row.length(); column++) {
                if (row.charAt(column) == '#') {
                    area++;
                }
            }
        }

        return area;
    }

    public List<PresentFootprint> transformations() {
        Set<PresentFootprint> transformations = new LinkedHashSet<>();
        List<GridCell> originalCells = occupiedCells();

        for (int rotation = 0; rotation < 4; rotation++) {
            for (boolean flipped : List.of(false, true)) {
                transformations.add(transform(originalCells, rotation, flipped));
            }
        }

        return List.copyOf(transformations);
    }

    private List<GridCell> occupiedCells() {
        List<GridCell> cells = new ArrayList<>();

        for (int row = 0; row < rows.size(); row++) {
            for (int column = 0; column < rows.get(row).length(); column++) {
                if (rows.get(row).charAt(column) == '#') {
                    cells.add(new GridCell(row, column));
                }
            }
        }

        return cells;
    }

    private PresentFootprint transform(
            List<GridCell> cells,
            int rotations,
            boolean flipped
    ) {
        List<GridCell> transformedCells = new ArrayList<>();

        for (GridCell cell : cells) {
            int row = cell.row();
            int column = cell.column();

            for (int i = 0; i < rotations; i++) {
                int nextRow = column;
                int nextColumn = -row;

                row = nextRow;
                column = nextColumn;
            }

            if (flipped) {
                column = -column;
            }

            transformedCells.add(new GridCell(row, column));
        }

        return normalize(transformedCells);
    }

    private PresentFootprint normalize(List<GridCell> cells) {
        int minRow = cells.stream()
                .mapToInt(GridCell::row)
                .min()
                .orElseThrow();

        int minColumn = cells.stream()
                .mapToInt(GridCell::column)
                .min()
                .orElseThrow();

        List<GridCell> normalizedCells = new ArrayList<>();

        for (GridCell cell : cells) {
            normalizedCells.add(new GridCell(
                    cell.row() - minRow,
                    cell.column() - minColumn
            ));
        }

        return new PresentFootprint(normalizedCells);
    }
}