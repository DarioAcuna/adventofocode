package es.ulpgc.aoc2025.day12;

import es.ulpgc.aoc2025.common.PuzzleSolver;
import es.ulpgc.aoc2025.day12.part1.Day12Part1Solver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day12Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
                Path.of("src/main/resources/day12/input.txt")
        );

        PuzzleSolver part1Solver = new Day12Part1Solver();

        long part1Result = part1Solver.solve(lines);

        System.out.println("Day 12 - Part 1: " + part1Result);
    }
}