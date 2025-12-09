package aoc.day09;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day09Test {

  private static final List<String> EXAMPLE_INPUT = List.of(
      "7,1",
      "11,1",
      "11,7",
      "9,7",
      "9,5",
      "2,5",
      "2,3",
      "7,3"
  );

  @Test
  void testPart1WithExampleData() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertEquals(50, theater.largestRectangleArea());
  }

  @Test
  void testPart2WithExampleData() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertEquals(24, theater.largestValidRectangleArea());
  }

  @Test
  void testPointsInsidePolygon() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertTrue(theater.testContainsPoint(9, 5), "(9,5) red tile");
    assertTrue(theater.testContainsPoint(2, 3), "(2,3) red tile");
    assertTrue(theater.testContainsPoint(9, 3), "(9,3) should be inside");
    assertTrue(theater.testContainsPoint(2, 5), "(2,5) red tile");
    assertFalse(theater.testContainsPoint(1, 1), "(1,1) outside");
    assertFalse(theater.testContainsPoint(12, 4), "(12,4) outside");
  }

  @Test
  void testRectangle15Area() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertTrue(theater.testContainsPoint(7, 3), "(7,3) red tile");
    assertTrue(theater.testContainsPoint(11, 1), "(11,1) red tile");
    assertTrue(theater.testContainsPoint(7, 1), "(7,1) red tile");
    assertTrue(theater.testContainsPoint(11, 3), "(11,3) should be inside");
  }

  @Test
  void testInvalidRectangleBetweenTwoFiveAndElevenOne() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertFalse(theater.testContainsPoint(3, 2), "(3,2) should be outside");
    assertFalse(theater.testContainsPoint(5, 2), "(5,2) should be outside");
  }

  @Test
  void testAllPointsIn24Rectangle() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    for (int x = 2; x <= 9; x++) {
      for (int y = 3; y <= 5; y++) {
          assertTrue(theater.testContainsPoint(x, y), "(" + x + "," + y + ") should be inside");
      }
    }
  }

  @Test
  void testRectangleWithPolygonEdgeCrossing() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertFalse(theater.testContainsPoint(7, 7), "(7,7) outside polygon");
    assertFalse(theater.testContainsPoint(7, 6), "(7,6) outside polygon");
  }

  @Test
  void testRectangleFromTwoThreeToElevenSeven() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertFalse(theater.testContainsPoint(2, 7), "(2,7) outside polygon");
    assertTrue(theater.testContainsPoint(11, 3), "(11,3) inside polygon");
  }

  @Test
  void testRectangleCrossedByPolygonEdge() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertFalse(theater.testContainsPoint(2, 1), "(2,1) outside");
  }

  @Test
  void testBuggyRectangle() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertTrue(theater.testContainsPoint(2, 3), "(2,3)");
    assertTrue(theater.testContainsPoint(9, 5), "(9,5)");
    assertTrue(theater.testContainsPoint(2, 5), "(2,5)");
    assertTrue(theater.testContainsPoint(9, 3), "(9,3)");

    assertTrue(theater.testContainsPoint(7, 3), "(7,3)");
    assertTrue(theater.testContainsPoint(11, 5), "(11,5)");
    assertTrue(theater.testContainsPoint(7, 5), "(7,5)");
    assertTrue(theater.testContainsPoint(11, 3), "(11,3)");
    assertTrue(theater.testContainsPoint(8, 4), "(8,4)");
  }

  @Test
  void testPotentialBugRectangle() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertTrue(theater.testContainsPoint(7, 1), "(7,1)");
    assertTrue(theater.testContainsPoint(9, 5), "(9,5)");
    assertTrue(theater.testContainsPoint(7, 5), "(7,5)");
    assertTrue(theater.testContainsPoint(9, 1), "(9,1)");
    assertTrue(theater.testContainsPoint(8, 2), "(8,2)");
  }

  @Test
  void testEdgeCrossingCase() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertTrue(theater.testContainsPoint(2, 3), "(2,3)");
    assertTrue(theater.testContainsPoint(11, 1), "(11,1)");
    assertFalse(theater.testContainsPoint(2, 1), "(2,1) outside");
  }

  @Test
  void testRectangleWithInternalEdge() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    assertTrue(theater.testContainsPoint(7, 3), "(7,3)");
    assertTrue(theater.testContainsPoint(9, 5), "(9,5)");
    assertTrue(theater.testContainsPoint(7, 5), "(7,5)");
    assertTrue(theater.testContainsPoint(9, 3), "(9,3)");
    assertTrue(theater.testContainsPoint(8, 4), "(8,4)");
  }
}
