package es.ulpgc.aoc2025.day12.part1;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day12.common.ChristmasTreeFarmReport;
import es.ulpgc.aoc2025.day12.common.ChristmasTreeFarmReportParser;
import es.ulpgc.aoc2025.day12.common.TreeRegionRequirement;

import java.util.List;

public class Day12Part1Solver implements PuzzleSolver {

    private final ChristmasTreeFarmReportParser parser = new ChristmasTreeFarmReportParser();
    private final PresentPackingAnalyzer analyzer = new PresentPackingAnalyzer();

    @Override
    public long solve(List<String> lines) {
        ChristmasTreeFarmReport report = parser.parse(lines);
        long fittingRegions = 0;

        for (TreeRegionRequirement region : report.regions()) {
            if (analyzer.canFit(report, region)) {
                fittingRegions++;
            }
        }

        return fittingRegions;
    }
}