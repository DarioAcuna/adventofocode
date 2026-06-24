package es.ulpgc.aoc2025.day11.part2;

import es.ulpgc.aoc2025.day11.common.DeviceNetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequiredDevicePathCounter {

    private static final String TARGET_DEVICE = "out";

    public long countPathsFrom(
            String startDevice,
            DeviceNetwork network,
            Set<String> requiredDevices
    ) {
        if (requiredDevices == null) {
            throw new IllegalArgumentException("Required devices cannot be null");
        }

        return countPathsFrom(
                startDevice,
                network,
                RequiredDeviceState.initial(requiredDevices),
                new HashMap<>(),
                new HashSet<>()
        );
    }

    private long countPathsFrom(
            String device,
            DeviceNetwork network,
            RequiredDeviceState state,
            Map<SearchState, Long> memo,
            Set<SearchState> visiting
    ) {
        RequiredDeviceState nextState = state.afterVisiting(device);
        SearchState searchState = new SearchState(device, nextState);

        if (device.equals(TARGET_DEVICE)) {
            return nextState.hasVisitedAllRequiredDevices() ? 1 : 0;
        }

        if (memo.containsKey(searchState)) {
            return memo.get(searchState);
        }

        if (visiting.contains(searchState)) {
            throw new IllegalStateException("Cycle detected from device: " + device);
        }

        visiting.add(searchState);

        long paths = 0;

        for (String output : network.outputsOf(device)) {
            paths += countPathsFrom(
                    output,
                    network,
                    nextState,
                    memo,
                    visiting
            );
        }

        visiting.remove(searchState);
        memo.put(searchState, paths);

        return paths;
    }

    private record SearchState(String device, RequiredDeviceState requiredDeviceState) {
    }

    private record RequiredDeviceState(Set<String> requiredDevices, Set<String> visitedRequiredDevices) {

        public RequiredDeviceState {
            requiredDevices = Set.copyOf(requiredDevices);
            visitedRequiredDevices = Set.copyOf(visitedRequiredDevices);
        }

        public static RequiredDeviceState initial(Set<String> requiredDevices) {
            return new RequiredDeviceState(requiredDevices, Set.of());
        }

        public RequiredDeviceState afterVisiting(String device) {
            if (!requiredDevices.contains(device)) {
                return this;
            }

            Set<String> updatedVisitedDevices = new HashSet<>(visitedRequiredDevices);
            updatedVisitedDevices.add(device);

            return new RequiredDeviceState(requiredDevices, updatedVisitedDevices);
        }

        public boolean hasVisitedAllRequiredDevices() {
            return visitedRequiredDevices.containsAll(requiredDevices);
        }
    }
}