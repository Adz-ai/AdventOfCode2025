package aoc.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.UseUnderscoresInNumericLiterals")
class Day02Test {

  private static final String EXAMPLE_INPUT =
      "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,"
          + "1698522-1698528,446443-446449,38593856-38593862,565653-565659,"
          + "824824821-824824827,2121212118-2121212124";

  private final Day02 day02 = new Day02();

  private static List<Range> parseRanges() {
    return Arrays.stream(EXAMPLE_INPUT.split(","))
        .map(Range::parse)
        .toList();
  }

  @Test
  void testPart1WithExampleData() {
    var result = day02.part1(parseRanges());
    assertEquals(1227775554L, result);
  }

  @Test
  void testPart2WithExampleData() {
    var result = day02.part2(parseRanges());
    assertEquals(4174379265L, result);
  }

  @Test
  void testPart2FindsMoreInvalidsThanPart1() {
    var ranges = List.of(new Range(95, 115));
    assertEquals(99L, day02.part1(ranges));
    assertEquals(210L, day02.part2(ranges));
  }

  @Test
  void testRangeParsing() {
    var range = Range.parse("  100-200  ");
    assertEquals(100L, range.start());
    assertEquals(200L, range.end());
  }
}
