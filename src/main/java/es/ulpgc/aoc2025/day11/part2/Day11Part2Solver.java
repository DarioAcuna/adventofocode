package es.ulpgc.aoc2025.day11.part2;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day11.common.DeviceNetwork;
import es.ulpgc.aoc2025.day11.common.DeviceNetworkParser;

import java.util.List;
import java.util.Set;

public class Day11Part2Solver implements PuzzleSolver {

    private static final String START_DEVICE = "svr";

    private static final Set<String> REQUIRED_DEVICES = Set.of(
            "dac",
            "fft"
    );

    private final DeviceNetworkParser parser = new DeviceNetworkParser();
    private final RequiredDevicePathCounter pathCounter = new RequiredDevicePathCounter();

    @Override
    public long solve(List<String> lines) {
        DeviceNetwork network = parser.parse(lines);

        return pathCounter.countPathsFrom(
                START_DEVICE,
                network,
                REQUIRED_DEVICES
        );
    }
}