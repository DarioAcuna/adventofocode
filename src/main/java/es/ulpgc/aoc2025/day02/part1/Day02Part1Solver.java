package es.ulpgc.aoc2025.day02.part1;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day02.common.IdRange;
import es.ulpgc.aoc2025.day02.common.IdRangeParser;
import es.ulpgc.aoc2025.day02.common.InvalidProductIdGenerator;

import java.util.List;
import java.util.Set;

public class Day02Part1Solver implements PuzzleSolver {

    private final IdRangeParser parser = new IdRangeParser();
    private final InvalidProductIdGenerator generator = new TwiceRepeatedProductIdGenerator();

    @Override
    public long solve(List<String> lines) {
        List<IdRange> ranges = parser.parse(lines);
        Set<Long> invalidIds = generator.generate(ranges);

        return invalidIds.stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}