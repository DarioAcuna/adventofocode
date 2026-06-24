package es.ulpgc.aoc2025.day05.part2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Part2SolverTest {

    @Test
    void shouldSolveOfficialExample() {
        List<String> input = List.of(
                "3-5",
                "10-14",
                "16-20",
                "12-18",
                "",
                "1",
                "5",
                "8",
                "11",
                "17",
                "32"
        );

        Day05Part2Solver solver = new Day05Part2Solver();

        assertEquals(14, solver.solve(input));
    }
}