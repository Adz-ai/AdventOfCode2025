package aoc.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day04Test {

  private static final String EMPTY_ROW = "...";

  private static final List<String> EXAMPLE_INPUT = List.of(
      "..@@.@@@@.",
      "@@@.@.@.@@",
      "@@@@@.@.@@",
      "@.@@@@..@.",
      "@@.@@@@.@@",
      ".@@@@@@@.@",
      ".@.@.@.@@@",
      "@.@@@.@@@@",
      ".@@@@@@@@.",
      "@.@.@@@.@."
  );

  @Test
  void testPart1WithExampleData() {
    var grid = new PaperRollGrid(EXAMPLE_INPUT);
    assertEquals(13L, grid.countAccessibleRolls());
  }

  @Test
  void testPart2WithExampleData() {
    var grid = new PaperRollGrid(EXAMPLE_INPUT);
    assertEquals(43L, grid.countTotalRemovableRolls());
  }

  @Test
  void testSingleAccessibleRoll() {
    var input = List.of(EMPTY_ROW, ".@.", EMPTY_ROW);
    var grid = new PaperRollGrid(input);
    assertEquals(1L, grid.countAccessibleRolls());
    assertEquals(1L, new PaperRollGrid(input).countTotalRemovableRolls());
  }

  @Test
  void testInaccessibleRollSurroundedByFour() {
    var input = List.of(".@.", "@@@", ".@.");
    var grid = new PaperRollGrid(input);
    assertEquals(4L, grid.countAccessibleRolls());
  }

  @Test
  void testEmptyGrid() {
    var input = List.of(EMPTY_ROW, EMPTY_ROW, EMPTY_ROW);
    var grid = new PaperRollGrid(input);
    assertEquals(0L, grid.countAccessibleRolls());
    assertEquals(0L, new PaperRollGrid(input).countTotalRemovableRolls());
  }
}
