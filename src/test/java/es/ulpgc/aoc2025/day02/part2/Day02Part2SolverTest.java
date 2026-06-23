package es.ulpgc.aoc2025.day02.part2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Part2SolverTest {

    @Test
    void shouldSolveOfficialExample() {
        List<String> input = List.of(
                "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,",
                "1698522-1698528,446443-446449,38593856-38593862,565653-565659,",
                "824824821-824824827,2121212118-2121212124"
        );

        Day02Part2Solver solver = new Day02Part2Solver();

        assertEquals(4174379265L, solver.solve(input));
    }
}