package es.ulpgc.aoc2025.day11.part1;

import es.ulpgc.aoc2025.day11.common.DeviceNetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DevicePathCounter {

    private static final String TARGET_DEVICE = "out";

    public long countPathsFrom(String startDevice, DeviceNetwork network) {
        return countPathsFrom(
                startDevice,
                network,
                new HashMap<>(),
                new HashSet<>()
        );
    }

    private long countPathsFrom(
            String device,
            DeviceNetwork network,
            Map<String, Long> memo,
            Set<String> visiting
    ) {
        if (device.equals(TARGET_DEVICE)) {
            return 1;
        }

        if (memo.containsKey(device)) {
            return memo.get(device);
        }

        if (visiting.contains(device)) {
            throw new IllegalStateException("Cycle detected from device: " + device);
        }

        visiting.add(device);

        long paths = 0;

        for (String output : network.outputsOf(device)) {
            paths += countPathsFrom(output, network, memo, visiting);
        }

        visiting.remove(device);
        memo.put(device, paths);

        return paths;
    }
}