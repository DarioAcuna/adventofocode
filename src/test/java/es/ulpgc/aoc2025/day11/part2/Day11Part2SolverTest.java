package es.ulpgc.aoc2025.day11.part2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Part2SolverTest {

    @Test
    void shouldSolveOfficialExample() {
        List<String> input = List.of(
                "svr: aaa bbb",
                "aaa: fft",
                "fft: ccc",
                "bbb: tty",
                "tty: ccc",
                "ccc: ddd eee",
                "ddd: hub",
                "hub: fff",
                "eee: dac",
                "dac: fff",
                "fff: ggg hhh",
                "ggg: out",
                "hhh: out"
        );

        Day11Part2Solver solver = new Day11Part2Solver();

        assertEquals(2, solver.solve(input));
    }
}