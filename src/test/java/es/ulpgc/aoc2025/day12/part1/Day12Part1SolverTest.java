package es.ulpgc.aoc2025.day12.part1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Part1SolverTest {

    @Test
    void shouldSolveOfficialExample() {
        List<String> input = List.of(
                "0:",
                "###",
                "##.",
                "##.",
                "",
                "1:",
                "###",
                "##.",
                ".##",
                "",
                "2:",
                ".##",
                "###",
                "##.",
                "",
                "3:",
                "##.",
                "###",
                "##.",
                "",
                "4:",
                "###",
                "#..",
                "###",
                "",
                "5:",
                "###",
                ".#.",
                "###",
                "",
                "4x4: 0 0 0 0 2 0",
                "12x5: 1 0 1 0 2 2",
                "12x5: 1 0 1 0 3 2"
        );

        Day12Part1Solver solver = new Day12Part1Solver();

        assertEquals(2, solver.solve(input));
    }
}