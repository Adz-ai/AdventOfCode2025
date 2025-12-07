package aoc.day01;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution for Advent of Code 2025 Day 1: Safe Lock.
 *
 * <p>Simulates a safe lock dial that rotates left or right based on input instructions.
 * Tracks how many times the dial passes through or lands on position zero.
 */
class Day01 {

  private static final Logger LOG = LoggerFactory.getLogger(Day01.class);
  private static final char LEFT_DIRECTION = 'L';

  /**
   * Runs the Day 1 solution.
   */
  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day01/input");
        var lock = runSimulation(lines);

        log.info("Part 1: {}", lock.getEndOfRotationZeroCount());
        log.info("Part 2: {}", lock.getZeroHitCount());

      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }

  /**
   * Runs the lock simulation by processing rotation instructions.
   *
   * @param lines  the rotation instructions, each starting with 'L' or 'R' followed by steps
   * @return the SafeLock after all rotations have been applied
   */
  SafeLock runSimulation(@NotNull List<String> lines) {
    var lock = new SafeLock();
    for (var line : lines) {
      int amount = Integer.parseInt(line.substring(1));
      if (line.charAt(0) == LEFT_DIRECTION) {
        lock.turnCounterClockwise(amount);
      } else {
        lock.turnClockwise(amount);
      }
    }
    return lock;
  }
}
