package es.ulpgc.aoc2025.day12.common;

import java.util.List;
import java.util.Map;

public record TreeRegionRequirement(
        int width,
        int height,
        List<Integer> quantities
) {

    public TreeRegionRequirement {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }

        if (quantities == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }

        quantities = List.copyOf(quantities);
    }

    public long area() {
        return (long) width * height;
    }

    public int presentCount() {
        return quantities.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public long requiredArea(Map<Integer, PresentShape> shapesByIndex) {
        long totalArea = 0;

        for (int shapeIndex = 0; shapeIndex < quantities.size(); shapeIndex++) {
            PresentShape shape = shapesByIndex.get(shapeIndex);

            if (shape == null) {
                throw new IllegalArgumentException("Missing shape: " + shapeIndex);
            }

            totalArea += (long) shape.area() * quantities.get(shapeIndex);
        }

        return totalArea;
    }
}