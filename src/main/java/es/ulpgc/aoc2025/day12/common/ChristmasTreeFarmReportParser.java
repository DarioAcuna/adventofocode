package es.ulpgc.aoc2025.day12.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChristmasTreeFarmReportParser {

    private static final Pattern SHAPE_HEADER_PATTERN = Pattern.compile("(\\d+):");
    private static final Pattern REGION_PATTERN = Pattern.compile("(\\d+)x(\\d+):\\s+(.+)");

    public ChristmasTreeFarmReport parse(List<String> lines) {
        Map<Integer, PresentShape> shapesByIndex = new HashMap<>();
        List<TreeRegionRequirement> regions = new ArrayList<>();

        int lineIndex = 0;

        while (lineIndex < lines.size()) {
            String line = lines.get(lineIndex).trim();

            if (line.isBlank()) {
                lineIndex++;
                continue;
            }

            Matcher shapeMatcher = SHAPE_HEADER_PATTERN.matcher(line);

            if (shapeMatcher.matches()) {
                int shapeIndex = Integer.parseInt(shapeMatcher.group(1));
                List<String> shapeRows = new ArrayList<>();

                lineIndex++;

                while (lineIndex < lines.size()) {
                    String shapeLine = lines.get(lineIndex).trim();

                    if (shapeLine.isBlank()) {
                        lineIndex++;
                        break;
                    }

                    if (SHAPE_HEADER_PATTERN.matcher(shapeLine).matches()
                            || REGION_PATTERN.matcher(shapeLine).matches()) {
                        break;
                    }

                    shapeRows.add(shapeLine);
                    lineIndex++;
                }

                shapesByIndex.put(shapeIndex, new PresentShape(shapeIndex, shapeRows));
                continue;
            }

            Matcher regionMatcher = REGION_PATTERN.matcher(line);

            if (regionMatcher.matches()) {
                regions.add(parseRegion(regionMatcher));
                lineIndex++;
                continue;
            }

            throw new IllegalArgumentException("Invalid line: " + line);
        }

        return new ChristmasTreeFarmReport(shapesByIndex, regions);
    }

    private TreeRegionRequirement parseRegion(Matcher matcher) {
        int width = Integer.parseInt(matcher.group(1));
        int height = Integer.parseInt(matcher.group(2));

        String[] rawQuantities = matcher.group(3).trim().split("\\s+");
        List<Integer> quantities = new ArrayList<>();

        for (String rawQuantity : rawQuantities) {
            quantities.add(Integer.parseInt(rawQuantity));
        }

        return new TreeRegionRequirement(width, height, quantities);
    }
}