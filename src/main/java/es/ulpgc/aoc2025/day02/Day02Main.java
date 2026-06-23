package es.ulpgc.aoc2025.day02;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day02.part1.Day02Part1Solver;
import es.ulpgc.aoc2025.day02.part2.Day02Part2Solver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day02Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Path.of("src/main/resources/day02/input.txt")
        );

        PuzzleSolver part1Solver = new Day02Part1Solver();
        PuzzleSolver part2Solver = new Day02Part2Solver();

        long part1Result = part1Solver.solve(lines);
        long part2Result = part2Solver.solve(lines);

        System.out.println("Day 02 - Part 1: " + part1Result);
        System.out.println("Day 02 - Part 2: " + part2Result);
    }
}