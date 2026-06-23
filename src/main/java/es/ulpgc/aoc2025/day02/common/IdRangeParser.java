package es.ulpgc.aoc2025.day02.common;

import java.util.ArrayList;
import java.util.List;

public class IdRangeParser {

    public List<IdRange> parse(List<String> lines) {
        String input = String.join("", lines).trim();

        if (input.isEmpty()) {
            return List.of();
        }

        String[] rawRanges = input.split(",");
        List<IdRange> ranges = new ArrayList<>();

        for (String rawRange : rawRanges) {
            if (rawRange.isBlank()) {
                continue;
            }

            ranges.add(parseRange(rawRange.trim()));
        }

        return ranges;
    }

    private IdRange parseRange(String rawRange) {
        String[] bounds = rawRange.split("-");

        if (bounds.length != 2) {
            throw new IllegalArgumentException("Invalid range: " + rawRange);
        }

        long firstId = Long.parseLong(bounds[0]);
        long lastId = Long.parseLong(bounds[1]);

        return new IdRange(firstId, lastId);
    }
}