package es.ulpgc.aoc2025.day11.part1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Part1SolverTest {

    @Test
    void shouldSolveOfficialExample() {
        List<String> input = List.of(
                "aaa: you hhh",
                "you: bbb ccc",
                "bbb: ddd eee",
                "ccc: ddd eee fff",
                "ddd: ggg",
                "eee: out",
                "fff: out",
                "ggg: out",
                "hhh: ccc fff iii",
                "iii: out"
        );

        Day11Part1Solver solver = new Day11Part1Solver();

        assertEquals(5, solver.solve(input));
    }
}