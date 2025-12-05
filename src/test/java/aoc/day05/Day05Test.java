package aoc.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day05Test {

  private static final List<FreshRange> EXAMPLE_RANGES = List.of(
      new FreshRange(3, 5),
      new FreshRange(10, 14),
      new FreshRange(16, 20),
      new FreshRange(12, 18)
  );

  private static final List<Long> EXAMPLE_INGREDIENTS = List.of(1L, 5L, 8L, 11L, 17L, 32L);

  private static final IngredientDatabase EXAMPLE_DATABASE =
      new IngredientDatabase(EXAMPLE_RANGES, EXAMPLE_INGREDIENTS);

  @Test
  void testPart1WithExampleData() {
    assertEquals(3L, EXAMPLE_DATABASE.countFreshIngredients());
  }

  @Test
  void testPart2WithExampleData() {
    assertEquals(14L, EXAMPLE_DATABASE.countTotalFreshIds());
  }

  @Test
  void testFreshRangeContains() {
    var range = new FreshRange(10, 14);
    assertFalse(range.contains(9));
    assertTrue(range.contains(10));
    assertTrue(range.contains(12));
    assertTrue(range.contains(14));
    assertFalse(range.contains(15));
  }

  @Test
  void testFreshRangeSize() {
    assertEquals(3L, new FreshRange(3, 5).size());
    assertEquals(5L, new FreshRange(10, 14).size());
    assertEquals(1L, new FreshRange(5, 5).size());
  }

  @Test
  void testFreshRangeParse() {
    var range = FreshRange.parse("10-14");
    assertEquals(10L, range.start());
    assertEquals(14L, range.end());
  }

  @Test
  void testOverlappingRangesMerge() {
    var ranges = List.of(
        new FreshRange(10, 14),
        new FreshRange(12, 18)
    );
    var database = new IngredientDatabase(ranges, List.of());
    assertEquals(9L, database.countTotalFreshIds());
  }

  @Test
  void testAdjacentRangesMerge() {
    var ranges = List.of(
        new FreshRange(1, 5),
        new FreshRange(6, 10)
    );
    var database = new IngredientDatabase(ranges, List.of());
    assertEquals(10L, database.countTotalFreshIds());
  }

  @Test
  void testNonOverlappingRanges() {
    var ranges = List.of(
        new FreshRange(1, 5),
        new FreshRange(10, 15)
    );
    var database = new IngredientDatabase(ranges, List.of());
    assertEquals(11L, database.countTotalFreshIds());
  }

  @Test
  void testEmptyDatabase() {
    var database = new IngredientDatabase(List.of(), List.of());
    assertEquals(0L, database.countFreshIngredients());
    assertEquals(0L, database.countTotalFreshIds());
  }
}
