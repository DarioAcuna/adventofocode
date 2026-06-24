package es.ulpgc.aoc2025.day12.common;

import java.util.List;
import java.util.Map;

public record ChristmasTreeFarmReport(
        Map<Integer, PresentShape> shapesByIndex,
        List<TreeRegionRequirement> regions
) {

    public ChristmasTreeFarmReport {
        if (shapesByIndex == null) {
            throw new IllegalArgumentException("Shapes cannot be null");
        }

        if (regions == null) {
            throw new IllegalArgumentException("Regions cannot be null");
        }

        if (shapesByIndex.isEmpty()) {
            throw new IllegalArgumentException("At least one shape is required");
        }

        shapesByIndex = Map.copyOf(shapesByIndex);
        regions = List.copyOf(regions);
    }

    public PresentShape shapeByIndex(int index) {
        PresentShape shape = shapesByIndex.get(index);

        if (shape == null) {
            throw new IllegalArgumentException("Missing shape: " + index);
        }

        return shape;
    }
}