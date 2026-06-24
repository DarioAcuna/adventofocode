package es.ulpgc.aoc2025.day05.part1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Part1SolverTest {

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

        Day05Part1Solver solver = new Day05Part1Solver();

        assertEquals(3, solver.solve(input));
    }
}