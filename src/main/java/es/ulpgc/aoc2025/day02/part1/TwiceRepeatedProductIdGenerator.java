package es.ulpgc.aoc2025.day02.part1;

import es.ulpgc.aoc2025.day02.common.IdRange;
import es.ulpgc.aoc2025.day02.common.InvalidProductIdGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwiceRepeatedProductIdGenerator implements InvalidProductIdGenerator {

    @Override
    public Set<Long> generate(List<IdRange> ranges) {
        long maxId = maxIdOf(ranges);
        int maxDigits = digitsOf(maxId);

        Set<Long> invalidIds = new HashSet<>();

        for (int blockLength = 1; blockLength <= maxDigits / 2; blockLength++) {
            long firstBlock = powerOfTen(blockLength - 1);
            long lastBlock = powerOfTen(blockLength) - 1;

            for (long block = firstBlock; block <= lastBlock; block++) {
                long id = Long.parseLong(String.valueOf(block) + block);

                if (id <= maxId && isInsideAnyRange(id, ranges)) {
                    invalidIds.add(id);
                }
            }
        }

        return invalidIds;
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