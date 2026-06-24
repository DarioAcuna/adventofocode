package es.ulpgc.aoc2025.day12.part1;

import es.ulpgc.aoc2025.day12.common.ChristmasTreeFarmReport;
import es.ulpgc.aoc2025.day12.common.GridCell;
import es.ulpgc.aoc2025.day12.common.PresentFootprint;
import es.ulpgc.aoc2025.day12.common.PresentShape;
import es.ulpgc.aoc2025.day12.common.TreeRegionRequirement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresentPackingAnalyzer {

    private static final int EXACT_SEARCH_MAX_CELLS = 63;

    public boolean canFit(
            ChristmasTreeFarmReport report,
            TreeRegionRequirement region
    ) {
        if (region.requiredArea(report.shapesByIndex()) > region.area()) {
            return false;
        }

        if (canFitUsingBoundingBoxes(report, region)) {
            return true;
        }

        if (region.area() <= EXACT_SEARCH_MAX_CELLS) {
            return canFitUsingExactSearch(report, region);
        }

        return false;
    }

    private boolean canFitUsingBoundingBoxes(
            ChristmasTreeFarmReport report,
            TreeRegionRequirement region
    ) {
        int boxWidth = maximumFootprintWidth(report);
        int boxHeight = maximumFootprintHeight(report);

        long boxesPerRow = region.width() / boxWidth;
        long boxRows = region.height() / boxHeight;

        return region.presentCount() <= boxesPerRow * boxRows;
    }

    private int maximumFootprintWidth(ChristmasTreeFarmReport report) {
        return report.shapesByIndex()
                .values()
                .stream()
                .flatMap(shape -> shape.transformations().stream())
                .mapToInt(PresentFootprint::width)
                .max()
                .orElseThrow();
    }

    private int maximumFootprintHeight(ChristmasTreeFarmReport report) {
        return report.shapesByIndex()
                .values()
                .stream()
                .flatMap(shape -> shape.transformations().stream())
                .mapToInt(PresentFootprint::height)
                .max()
                .orElseThrow();
    }

    private boolean canFitUsingExactSearch(
            ChristmasTreeFarmReport report,
            TreeRegionRequirement region
    ) {
        List<Integer> presents = expandedPresentList(region);
        presents.sort((first, second) -> Integer.compare(
                placementsOf(report.shapeByIndex(first), region).size(),
                placementsOf(report.shapeByIndex(second), region).size()
        ));

        return search(
                report,
                region,
                presents,
                0,
                0L,
                new HashSet<>()
        );
    }

    private List<Integer> expandedPresentList(TreeRegionRequirement region) {
        List<Integer> presents = new ArrayList<>();

        for (int shapeIndex = 0; shapeIndex < region.quantities().size(); shapeIndex++) {
            int quantity = region.quantities().get(shapeIndex);

            for (int i = 0; i < quantity; i++) {
                presents.add(shapeIndex);
            }
        }

        return presents;
    }

    private boolean search(
            ChristmasTreeFarmReport report,
            TreeRegionRequirement region,
            List<Integer> presents,
            int presentIndex,
            long occupiedCells,
            Set<SearchState> failedStates
    ) {
        if (presentIndex == presents.size()) {
            return true;
        }

        SearchState state = new SearchState(presentIndex, occupiedCells);

        if (failedStates.contains(state)) {
            return false;
        }

        PresentShape shape = report.shapeByIndex(presents.get(presentIndex));

        for (long placement : placementsOf(shape, region)) {
            if ((occupiedCells & placement) != 0) {
                continue;
            }

            if (search(
                    report,
                    region,
                    presents,
                    presentIndex + 1,
                    occupiedCells | placement,
                    failedStates
            )) {
                return true;
            }
        }

        failedStates.add(state);
        return false;
    }

    private List<Long> placementsOf(
            PresentShape shape,
            TreeRegionRequirement region
    ) {
        Set<Long> placements = new HashSet<>();

        for (PresentFootprint footprint : shape.transformations()) {
            for (int row = 0; row <= region.height() - footprint.height(); row++) {
                for (int column = 0; column <= region.width() - footprint.width(); column++) {
                    placements.add(maskOf(footprint, row, column, region.width()));
                }
            }
        }

        return List.copyOf(placements);
    }

    private long maskOf(
            PresentFootprint footprint,
            int rowOffset,
            int columnOffset,
            int regionWidth
    ) {
        long mask = 0;

        for (GridCell cell : footprint.cells()) {
            int row = rowOffset + cell.row();
            int column = columnOffset + cell.column();
            int bitIndex = row * regionWidth + column;

            mask |= 1L << bitIndex;
        }

        return mask;
    }

    private record SearchState(int presentIndex, long occupiedCells) {
    }
}