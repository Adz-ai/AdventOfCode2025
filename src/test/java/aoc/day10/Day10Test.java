package aoc.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day10Test {

  private static final List<String> EXAMPLE_INPUT = List.of(
      "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
      "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
      "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
  );

  @Test
  void testExamplePart1() {
    var factory = Factory.parse(EXAMPLE_INPUT);
    assertEquals(7, factory.minTotalButtonPresses());
  }

  @Test
  void testMachine1() {
    // [.##.] needs lights 1 and 2 on (0-indexed)
    // Minimum is 2 presses: (0,2) and (0,1)
    // (0,2) toggles lights 0,2 -> [#.#.]
    // (0,1) toggles lights 0,1 -> [.##.]
    var factory = Factory.parse(List.of(
        "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
    ));
    assertEquals(2, factory.minTotalButtonPresses());
  }

  @Test
  void testMachine2() {
    // [...#.] needs light 3 on
    // Minimum is 3 presses
    var factory = Factory.parse(List.of(
        "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}"
    ));
    assertEquals(3, factory.minTotalButtonPresses());
  }

  @Test
  void testMachine3() {
    // [.###.#] needs lights 1,2,3,5 on
    // Minimum is 2 presses
    var factory = Factory.parse(List.of(
        "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
    ));
    assertEquals(2, factory.minTotalButtonPresses());
  }

  @Test
  void testExamplePart2() {
    var factory = Factory.parse(EXAMPLE_INPUT);
    assertEquals(33, factory.minTotalJoltagePresses());
  }

  @Test
  void testMachine1Part2() {
    // {3,5,4,7} - need counters to reach these values
    // Minimum 10 presses: (3) once, (1,3) 3x, (2,3) 3x, (0,2) once, (0,1) 2x
    var factory = Factory.parse(List.of(
        "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
    ));
    assertEquals(10, factory.minTotalJoltagePresses());
  }

  @Test
  void testMachine2Part2() {
    // {7,5,12,7,2} - minimum 12 presses
    var factory = Factory.parse(List.of(
        "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}"
    ));
    assertEquals(12, factory.minTotalJoltagePresses());
  }

  @Test
  void testMachine3Part2() {
    // {10,11,11,5,10,5} - minimum 11 presses
    var factory = Factory.parse(List.of(
        "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
    ));
    assertEquals(11, factory.minTotalJoltagePresses());
  }
}
