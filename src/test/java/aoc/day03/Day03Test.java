package aoc.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day03Test {

  private final Day03 day03 = new Day03();

  @Test
  void testMaxJoltageSingleDigit() {
    assertEquals(9L, day03.maxJoltage("123456789", 1));
  }

  @Test
  void testMaxJoltageTwoDigits() {
    assertEquals(99L, day03.maxJoltage("192939495969798999", 2));
  }

  @Test
  void testMaxJoltageSelectsLargestPossible() {
    assertEquals(95L, day03.maxJoltage("12395", 2));
  }

  @Test
  void testMaxJoltageGreedySelection() {
    // From "987654321", pick 2 digits: first can be from indices 0-7, max is 9 at 0
    // Second must come after index 0, max is 8 at index 1
    assertEquals(98L, day03.maxJoltage("987654321", 2));
  }

  @Test
  void testMaxJoltageAllSameDigits() {
    assertEquals(55L, day03.maxJoltage("55555", 2));
  }

  @Test
  void testMaxJoltageEntireString() {
    assertEquals(12_345L, day03.maxJoltage("12345", 5));
  }

  @Test
  void testMaxJoltageTwelveDigits() {
    assertEquals(999_999_999_999L, day03.maxJoltage("999999999999", 12));
  }

  @Test
  void testTotalOutputJoltageSingleBank() {
    var lines = List.of("12345");
    assertEquals(45L, day03.totalOutputJoltage(lines, 2));
  }

  @Test
  void testTotalOutputJoltageMultipleBanks() {
    var lines = List.of("123", "456", "789");
    assertEquals(23L + 56L + 89L, day03.totalOutputJoltage(lines, 2));
  }

  @Test
  void testTotalOutputJoltageEmptyList() {
    assertEquals(0L, day03.totalOutputJoltage(List.of(), 2));
  }

  @Test
  void testMaxJoltageDescendingOrder() {
    assertEquals(98L, day03.maxJoltage("98765", 2));
  }

  @Test
  void testMaxJoltageAscendingOrder() {
    assertEquals(45L, day03.maxJoltage("12345", 2));
  }
}
