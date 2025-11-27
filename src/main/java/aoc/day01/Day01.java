package aoc.day01;

import aoc.util.FileUtils;

import java.io.IOException;
import java.util.List;

public class Day01 {

    void main() {
        try {
            var lines = FileUtils.readLines("day01/input");

            System.out.println("Part 1: " + part1(lines));
            System.out.println("Part 2: " + part2(lines));

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    int part1(List<String> lines) {
        return 0;
    }

    int part2(List<String> lines) {
        return 0;
    }
}
