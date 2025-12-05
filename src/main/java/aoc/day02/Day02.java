package aoc.day02;

import aoc.util.FileUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day02 {
  private static final Logger LOG = LoggerFactory.getLogger(Day02.class);

  void main() {
    try {
      var inputString = FileUtils.readAsString("day02/input");
      var ranges = Arrays.stream(inputString.split(","))
          .map(Range::parse)
          .toList();

      LOG.info("Part 1: {}", part1(ranges));
      LOG.info("Part 2: {}", part2(ranges));

    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
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
   * Regex: ^(.+)\1$
   *   ^      - start of string
   *   (.+)   - capture group: one or more characters (the pattern)
   *   \1     - backreference: the same pattern repeated exactly once
   *   $      - end of string
   */
  private boolean isInvalidId(long id) {
    return String.valueOf(id).matches("^(.+)\\1$");
  }

  /**
   * Part 2: ID is invalid if it consists of a pattern repeated 2+ times.
   * Regex: ^(.+)\1+$
   *   ^      - start of string
   *   (.+)   - capture group: one or more characters (the pattern)
   *   \1+    - backreference: the same pattern repeated one or more times
   *   $      - end of string
   */
  private boolean isInvalidIdPart2(long id) {
    return String.valueOf(id).matches("^(.+)\\1+$");
  }
}
