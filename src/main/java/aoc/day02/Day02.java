package aoc.day02;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day02 {
  private static final Logger LOG = LoggerFactory.getLogger(Day02.class);

  private static final long[] POWERS_OF_10 = {
      1L, 10L, 100L, 1_000L, 10_000L, 100_000L, 1_000_000L, 10_000_000L,
      100_000_000L, 1_000_000_000L, 10_000_000_000L, 100_000_000_000L,
      1_000_000_000_000L, 10_000_000_000_000L, 100_000_000_000_000L,
      1_000_000_000_000_000L, 10_000_000_000_000_000L, 100_000_000_000_000_000L,
      1_000_000_000_000_000_000L
  };

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var inputString = FileUtils.readAsString("day02/input");
        var ranges = Arrays.stream(inputString.split(","))
            .map(Range::parse)
            .toList();

        log.info("Part 1: {}", part1(ranges));
        log.info("Part 2: {}", part2(ranges));

      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }

  long part1(List<Range> ranges) {
    return sumInvalidIds(ranges, this::isInvalidId);
  }

  long part2(List<Range> ranges) {
    return sumInvalidIds(ranges, this::isInvalidIdPart2);
  }

  private long sumInvalidIds(@NotNull List<Range> ranges, LongPredicate isInvalid) {
    return ranges.stream()
        .flatMapToLong(Range::stream)
        .filter(isInvalid)
        .sum();
  }

  /**
   * Part 1: ID is invalid if it consists of exactly two identical halves.
   * Example: 1212 -> "12" repeated twice -> invalid
   */
  private boolean isInvalidId(long id) {
    int len = digitCount(id);
    if (len % 2 != 0) {
      return false;
    }

    long divisor = powerOf10(len / 2);
    long firstHalf = id / divisor;
    long secondHalf = id % divisor;
    return firstHalf == secondHalf;
  }

  /**
   * Part 2: ID is invalid if it consists of a pattern repeated 2+ times.
   * Example: 123123123 -> "123" repeated 3 times -> invalid
   */
  private boolean isInvalidIdPart2(long id) {
    int len = digitCount(id);

    for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
      if (len % patternLen == 0 && isRepeatedPattern(id, len, patternLen)) {
        return true;
      }
    }
    return false;
  }

  private boolean isRepeatedPattern(long id, int len, int patternLen) {
    long divisor = powerOf10(patternLen);
    long pattern = id % divisor;

    long remaining = id;
    int remainingLen = len;

    while (remainingLen > 0) {
      if (remaining % divisor != pattern) {
        return false;
      }
      remaining /= divisor;
      remainingLen -= patternLen;
    }
    return true;
  }

  private static int digitCount(long n) {
    for (int i = 1; i < POWERS_OF_10.length; i++) {
      if (n < POWERS_OF_10[i]) {
        return i;
      }
    }
    return POWERS_OF_10.length;
  }

  private static long powerOf10(int exp) {
    return POWERS_OF_10[exp];
  }
}
