package es.ulpgc.aoc2025.day11.part1;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day11.common.DeviceNetwork;
import es.ulpgc.aoc2025.day11.common.DeviceNetworkParser;

import java.util.List;

public class Day11Part1Solver implements PuzzleSolver {

    private static final String START_DEVICE = "you";

    private final DeviceNetworkParser parser = new DeviceNetworkParser();
    private final DevicePathCounter pathCounter = new DevicePathCounter();

    @Override
    public long solve(List<String> lines) {
        DeviceNetwork network = parser.parse(lines);

        return pathCounter.countPathsFrom(START_DEVICE, network);
    }
}