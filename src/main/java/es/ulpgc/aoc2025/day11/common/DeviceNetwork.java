package es.ulpgc.aoc2025.day11.common;

import java.util.List;
import java.util.Map;

public record DeviceNetwork(Map<String, List<String>> outputsByDevice) {

    public DeviceNetwork {
        if (outputsByDevice == null) {
            throw new IllegalArgumentException("Outputs by device cannot be null");
        }

        outputsByDevice = Map.copyOf(outputsByDevice);
    }

    public List<String> outputsOf(String device) {
        return outputsByDevice.getOrDefault(device, List.of());
    }
}