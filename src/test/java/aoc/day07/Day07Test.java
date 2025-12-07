package aoc.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day07Test {

  private static final String EMPTY_ROW_15 = "...............";
  private static final String EMPTY_ROW_3 = "...";

  private static final List<String> EXAMPLE_INPUT = List.of(
      ".......S.......",
      EMPTY_ROW_15,
      ".......^.......",
      EMPTY_ROW_15,
      "......^.^......",
      EMPTY_ROW_15,
      ".....^.^.^.....",
      EMPTY_ROW_15,
      "....^.^...^....",
      EMPTY_ROW_15,
      "...^.^...^.^...",
      EMPTY_ROW_15,
      "..^...^.....^..",
      EMPTY_ROW_15,
      ".^.^.^.^.^...^.",
      EMPTY_ROW_15
  );

  @Test
  void testPart1WithExampleData() {
    var manifold = TachyonManifold.parse(EXAMPLE_INPUT);
    assertEquals(21, manifold.countSplits());
  }

  @Test
  void testSimpleSplit() {
    var input = List.of(
        ".S.",
        EMPTY_ROW_3,
        ".^.",
        EMPTY_ROW_3
    );
    var manifold = TachyonManifold.parse(input);
    assertEquals(1, manifold.countSplits());
  }

  @Test
  void testTwoSplits() {
    var input = List.of(
        ".S.",
        EMPTY_ROW_3,
        ".^.",
        EMPTY_ROW_3,
        "^.^"
    );
    var manifold = TachyonManifold.parse(input);
    // First split at row 2, then each beam hits a splitter at row 4
    assertEquals(3, manifold.countSplits());
  }

  @Test
  void testPart2WithExampleData() {
    var manifold = TachyonManifold.parse(EXAMPLE_INPUT);
    assertEquals(40, manifold.countTimelines());
  }

  @Test
  void testPart2SimpleSplit() {
    var input = List.of(
        ".S.",
        EMPTY_ROW_3,
        ".^.",
        EMPTY_ROW_3
    );
    var manifold = TachyonManifold.parse(input);
    // One splitter creates 2 timelines (left and right)
    assertEquals(2, manifold.countTimelines());
  }
}
