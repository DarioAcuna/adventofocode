package es.ulpgc.aoc2025.day11.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceNetworkParser {

    public DeviceNetwork parse(List<String> lines) {
        Map<String, List<String>> outputsByDevice = new HashMap<>();

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            parseConnectionLine(line.trim(), outputsByDevice);
        }

        return new DeviceNetwork(outputsByDevice);
    }

    private void parseConnectionLine(
            String line,
            Map<String, List<String>> outputsByDevice
    ) {
        String[] parts = line.split(":");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid device line: " + line);
        }

        String device = parts[0].trim();
        String outputsText = parts[1].trim();

        if (device.isEmpty()) {
            throw new IllegalArgumentException("Device name cannot be empty");
        }

        outputsByDevice.put(device, parseOutputs(outputsText));
    }

    private List<String> parseOutputs(String outputsText) {
        if (outputsText.isBlank()) {
            return List.of();
        }

        String[] rawOutputs = outputsText.split("\\s+");
        List<String> outputs = new ArrayList<>();

        for (String output : rawOutputs) {
            outputs.add(output.trim());
        }

        return outputs;
    }
}