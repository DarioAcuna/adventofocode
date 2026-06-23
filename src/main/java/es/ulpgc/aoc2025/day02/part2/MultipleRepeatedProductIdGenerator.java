package es.ulpgc.aoc2025.day02.part2;

import es.ulpgc.aoc2025.day02.common.IdRange;
import es.ulpgc.aoc2025.day02.common.InvalidProductIdGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleRepeatedProductIdGenerator implements InvalidProductIdGenerator {

    @Override
    public Set<Long> generate(List<IdRange> ranges) {
        long maxId = maxIdOf(ranges);
        int maxDigits = digitsOf(maxId);

        Set<Long> invalidIds = new HashSet<>();

        for (int totalLength = 2; totalLength <= maxDigits; totalLength++) {
            for (int blockLength = 1; blockLength <= totalLength / 2; blockLength++) {
                if (totalLength % blockLength != 0) {
                    continue;
                }

                int repetitions = totalLength / blockLength;
                generateIdsWith(totalLength, blockLength, repetitions, maxId, ranges, invalidIds);
            }
        }

        return invalidIds;
    }

    private void generateIdsWith(
            int totalLength,
            int blockLength,
            int repetitions,
            long maxId,
            List<IdRange> ranges,
            Set<Long> invalidIds
    ) {
        long firstBlock = powerOfTen(blockLength - 1);
        long lastBlock = powerOfTen(blockLength) - 1;

        for (long block = firstBlock; block <= lastBlock; block++) {
            String repeated = String.valueOf(block).repeat(repetitions);

            if (repeated.length() != totalLength) {
                continue;
            }

            long id = Long.parseLong(repeated);

            if (id <= maxId && isInsideAnyRange(id, ranges)) {
                invalidIds.add(id);
            }
        }
    }

    private long maxIdOf(List<IdRange> ranges) {
        return ranges.stream()
                .mapToLong(IdRange::lastId)
                .max()
                .orElse(0);
    }

    private int digitsOf(long value) {
        return String.valueOf(value).length();
    }

    private long powerOfTen(int exponent) {
        long result = 1;

        for (int i = 0; i < exponent; i++) {
            result *= 10;
        }

        return result;
    }

    private boolean isInsideAnyRange(long id, List<IdRange> ranges) {
        for (IdRange range : ranges) {
            if (range.contains(id)) {
                return true;
            }
        }

        return false;
    }
}