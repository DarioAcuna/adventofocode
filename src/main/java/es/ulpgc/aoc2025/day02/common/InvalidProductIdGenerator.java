package es.ulpgc.aoc2025.day02.common;

import java.util.List;
import java.util.Set;

public interface InvalidProductIdGenerator {

    Set<Long> generate(List<IdRange> ranges);
}