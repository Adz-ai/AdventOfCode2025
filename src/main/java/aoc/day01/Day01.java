package aoc.day01;

import aoc.util.FileUtils;

import java.io.IOException;
import java.util.List;

public class Day01 {

    void main() {
        try {
            var lines = FileUtils.readLines("day01/input");
            var lock = runSimulation(lines);

            System.out.println("Part 1: " + lock.getEndOfRotationZeroCount());
            System.out.println("Part 2: " + lock.getZeroHitCount());

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    SafeLock runSimulation(List<String> lines) {
        var lock = new SafeLock();
        for (var line : lines) {
            var split = line.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            var direction = split[0].charAt(0);
            var amount = Integer.parseInt(split[1]);
            if (direction == 'L') {
                lock.turnCounterClockwise(amount);
            } else {
                lock.turnClockwise(amount);
            }
        }
        return lock;
    }
}
