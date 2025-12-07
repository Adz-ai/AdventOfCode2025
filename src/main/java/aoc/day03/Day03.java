package aoc.day03;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution for Advent of Code 2025 Day 3: Lobby Battery Banks.
 *
 * <p>Calculates maximum joltage from battery banks by selecting digits that form
 * the largest possible number when concatenated in order.
 */
public class Day03 {

  private static final Logger LOG = LoggerFactory.getLogger(Day03.class);

  private static final int PART2_DIGITS = 12;
  private static final int MAX_DIGIT = 9;

  /**
   * Runs the Day 3 solution.
   */
  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day03/input");
        log.info("Part 1: {}", totalOutputJoltage(lines, 2));
        log.info("Part 2: {}", totalOutputJoltage(lines, PART2_DIGITS));
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }

  /**
   * Calculates the total output joltage by summing maximum joltage from each bank.
   *
   * @param lines       the battery banks, each line containing digit characters
   * @param digitCount  the number of digits to select from each bank
   * @return the sum of maximum joltages from all banks
   */
  long totalOutputJoltage(@NotNull List<String> lines, int digitCount) {
    return lines.stream()
        .mapToLong(bank -> maxJoltage(bank, digitCount))
        .sum();
  }

  /**
   * Finds the maximum joltage from a single bank by selecting digits greedily.
   *
   * <p>Uses a greedy algorithm: for each position, selects the largest digit
   * that still leaves enough remaining digits to complete the selection.
   *
   * @param bank        the battery bank as a string of digit characters
   * @param digitCount  the number of digits to select
   * @return the maximum joltage as a long value
   */
  long maxJoltage(@NotNull String bank, int digitCount) {
    var result = new StringBuilder();
    int startIdx = 0;

    for (int remaining = digitCount; remaining > 0; remaining--) {
      int maxDigit = -1;
      int maxIdx = -1;
      int lastValidIdx = bank.length() - remaining;

      for (int i = startIdx; i <= lastValidIdx; i++) {
        int digit = bank.charAt(i) - '0';
        if (digit > maxDigit) {
          maxDigit = digit;
          maxIdx = i;
          if (digit == MAX_DIGIT) {
            break;
          }
        }
      }

      result.append((char) ('0' + maxDigit));
      startIdx = maxIdx + 1;
    }

    return Long.parseLong(result.toString());
  }
}
