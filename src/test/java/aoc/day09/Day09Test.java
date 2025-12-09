package aoc.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

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
    // Largest rectangle: between 2,5 and 11,1
    // Width = |11-2| + 1 = 10, Height = |5-1| + 1 = 5
    // Area = 10 * 5 = 50
    assertEquals(50, theater.largestRectangleArea());
  }

  @Test
  void testPart2WithExampleData() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Largest valid rectangle: between 9,5 and 2,3
    // Width = |9-2| + 1 = 8, Height = |5-3| + 1 = 3
    // Area = 8 * 3 = 24
    assertEquals(24, theater.largestValidRectangleArea());
  }

  @Test
  void testPointsInsidePolygon() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Test some known points
    // (9,3) should be inside - it's on the top edge of the 24-area rectangle
    // (5,4) should be inside the polygon
    // (1,1) should be outside
    // (12,4) should be outside

    // The rectangle 9,5 to 2,3 has corners: (9,5), (2,3), (9,3), (2,5)
    // All should be inside or on boundary
    assertEquals(true, theater.testContainsPoint(9, 5), "(9,5) red tile");
    assertEquals(true, theater.testContainsPoint(2, 3), "(2,3) red tile");
    assertEquals(true, theater.testContainsPoint(9, 3), "(9,3) should be inside");
    assertEquals(true, theater.testContainsPoint(2, 5), "(2,5) red tile");

    // Test a point clearly outside
    assertEquals(false, theater.testContainsPoint(1, 1), "(1,1) outside");
    assertEquals(false, theater.testContainsPoint(12, 4), "(12,4) outside");
  }

  @Test
  void testRectangle15Area() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle between 7,3 and 11,1 should have area 15
    // Width = |11-7| + 1 = 5, Height = |3-1| + 1 = 3
    // Area = 5 * 3 = 15
    // Corners: (7,3), (11,1), (7,1), (11,3)
    assertEquals(true, theater.testContainsPoint(7, 3), "(7,3) red tile");
    assertEquals(true, theater.testContainsPoint(11, 1), "(11,1) red tile");
    assertEquals(true, theater.testContainsPoint(7, 1), "(7,1) red tile");
    assertEquals(true, theater.testContainsPoint(11, 3), "(11,3) should be inside");
  }

  @Test
  void testInvalidRectangleBetweenTwoFiveAndElevenOne() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle between 2,5 and 11,1 would have area 50 but is INVALID
    // because it includes tiles outside the polygon
    // Let's check point (3,2) which should be OUTSIDE
    // Y=2: .......X...X..  - only x=7 to x=11 are inside at y=2
    assertEquals(false, theater.testContainsPoint(3, 2), "(3,2) should be outside");
    assertEquals(false, theater.testContainsPoint(5, 2), "(5,2) should be outside");
  }

  @Test
  void testAllPointsIn24Rectangle() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle 9,5 to 2,3 - all points inside should be valid
    // X from 2 to 9, Y from 3 to 5
    for (int x = 2; x <= 9; x++) {
      for (int y = 3; y <= 5; y++) {
        assertEquals(true, theater.testContainsPoint(x, y),
            "(" + x + "," + y + ") should be inside");
      }
    }
  }

  @Test
  void testRectangleWithPolygonEdgeCrossing() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle from (7,1) to (9,7) - corners are all on boundary
    // But the polygon edge from (9,5) to (9,7) goes through the rectangle
    // Actually wait, (7,1) and (9,7) - let's check the other corners
    // (7,7) and (9,1)
    // (7,7) - is this inside? Let's check
    assertEquals(false, theater.testContainsPoint(7, 7), "(7,7) outside polygon");
    assertEquals(false, theater.testContainsPoint(7, 6), "(7,6) outside polygon");
  }

  @Test
  void testRectangleFromTwoThreeToElevenSeven() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle from (2,3) to (11,7)
    // This would include points outside the polygon
    // The other corners are (2,7) and (11,3)
    // (2,7) is outside the polygon
    assertEquals(false, theater.testContainsPoint(2, 7), "(2,7) outside polygon");
    // (11,3) should be inside (between vertical edge at x=11 from y=1 to y=7)
    assertEquals(true, theater.testContainsPoint(11, 3), "(11,3) inside polygon");
  }

  @Test
  void testRectangleCrossedByPolygonEdge() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle from (2,5) to (11,1)
    // Corners: (2,5) red, (11,1) red, (2,1), (11,5)
    // (2,1) - outside polygon
    assertEquals(false, theater.testContainsPoint(2, 1), "(2,1) outside");
    // So this rectangle should be INVALID
    // But even if it had all 4 corners inside, there's a polygon edge at x=7
    // from (7,1) to (7,3) that cuts through any rectangle spanning x<7 to x>7
    // at y<3
  }

  @Test
  void testBuggyRectangle() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Find a rectangle where all 4 corners are inside but polygon edge crosses interior
    // Rectangle from (2,3) to (9,5)
    // Corners: (2,3) red, (9,5) red, (2,5) red, (9,3)
    assertEquals(true, theater.testContainsPoint(2, 3), "(2,3)");
    assertEquals(true, theater.testContainsPoint(9, 5), "(9,5)");
    assertEquals(true, theater.testContainsPoint(2, 5), "(2,5)");
    assertEquals(true, theater.testContainsPoint(9, 3), "(9,3)");
    // All 4 corners are inside! And area would be 8*3=24

    // Now let's check rectangle from (7,3) to (11,5)
    // Corners: (7,3) red, (11,5), (7,5), (11,3)
    assertEquals(true, theater.testContainsPoint(7, 3), "(7,3)");
    assertEquals(true, theater.testContainsPoint(11, 5), "(11,5)");
    assertEquals(true, theater.testContainsPoint(7, 5), "(7,5)");
    assertEquals(true, theater.testContainsPoint(11, 3), "(11,3)");
    // All corners inside! But is (8,4) inside?
    assertEquals(true, theater.testContainsPoint(8, 4), "(8,4)");
    // This should be inside, so this rectangle is valid
  }

  @Test
  void testPotentialBugRectangle() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Let me find a case where checking corners isn't enough
    // Rectangle from (7,1) to (9,5)
    // Corners: (7,1) red, (9,5) red, (7,5), (9,1)
    assertEquals(true, theater.testContainsPoint(7, 1), "(7,1)");
    assertEquals(true, theater.testContainsPoint(9, 5), "(9,5)");
    assertEquals(true, theater.testContainsPoint(7, 5), "(7,5)");
    assertEquals(true, theater.testContainsPoint(9, 1), "(9,1)");
    // All 4 corners inside! Area = 3*5 = 15
    // But what about (8,2)? Is it inside?
    assertEquals(true, theater.testContainsPoint(8, 2), "(8,2)");
    // What about (7,4)? Between (7,3) and (7,5) but x=7 only has edge from y=1 to y=3
    // Wait, let me re-check the polygon edges
  }

  @Test
  void testEdgeCrossingCase() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle from (2,3) to (11,5)
    // All 4 corners: (2,3) red, (11,5), (2,5) red, (11,3)
    // All corners are inside! But there's a vertical edge at x=9 from y=5 to y=7
    // that passes through... wait no, that's y=5 to y=7, not inside y=3 to y=5
    // Let me check x=7, edge from y=1 to y=3 - this passes through y=3
    // which is ON the boundary of the rectangle, not interior

    // Try rectangle from (2,3) to (11,1)
    // Corners: (2,3) red, (11,1) red, (2,1), (11,3)
    assertEquals(true, theater.testContainsPoint(2, 3), "(2,3)");
    assertEquals(true, theater.testContainsPoint(11, 1), "(11,1)");
    assertEquals(false, theater.testContainsPoint(2, 1), "(2,1) outside");
    // So this rectangle is already invalid due to corner check
  }

  @Test
  void testRectangleWithInternalEdge() {
    var theater = MovieTheater.parse(EXAMPLE_INPUT);
    // Rectangle from (7,3) to (9,5)
    // Corners: (7,3) red, (9,5) red, (7,5), (9,3)
    // All corners should be inside
    // But there is NO edge inside this rectangle's interior
    // This should be valid with area = 3 * 3 = 9
    assertEquals(true, theater.testContainsPoint(7, 3), "(7,3)");
    assertEquals(true, theater.testContainsPoint(9, 5), "(9,5)");
    assertEquals(true, theater.testContainsPoint(7, 5), "(7,5)");
    assertEquals(true, theater.testContainsPoint(9, 3), "(9,3)");
    // Check interior point
    assertEquals(true, theater.testContainsPoint(8, 4), "(8,4)");
  }
}
