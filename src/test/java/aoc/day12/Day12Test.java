package aoc.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class Day12Test {

  private static final List<String> EXAMPLE_INPUT = List.of(
      "0:",
      "##.",
      "##.",
      "###",
      "",
      "1:",
      "###",
      "..#",
      "###",
      "",
      "2:",
      "###",
      ".##",
      "..#",
      "",
      "3:",
      ".##",
      "##.",
      "###",
      "",
      "4:",
      "###",
      ".#.",
      "###",
      "",
      "5:",
      "#..",
      "##.",
      ".##",
      "",
      "4x4: 2 0 0 0 0 0",
      "5x5: 1 1 0 0 0 0",
      "10x10: 0 0 0 0 0 0",
      "3x3: 1 0 0 0 0 0",
      "2x2: 1 0 0 0 0 0"
  );

  private static final List<String> SHAPE_0_LINES = List.of("##.", "##.", "###");

  @Test
  void testShapeParsing() {
    var shape = Shape.parse(SHAPE_0_LINES);
    assertEquals(7, shape.size());
    assertEquals(3, shape.minDimension());
  }

  @Test
  void testRegionParsing() {
    var region = Region.parse("12x5: 1 0 1 0 2 2");
    assertEquals(12, region.width());
    assertEquals(5, region.height());
    assertEquals(60, region.area());
    assertEquals(5, region.minDimension());
    assertEquals(6, region.presentCounts().length);
    assertEquals(1, region.presentCounts()[0]);
    assertEquals(0, region.presentCounts()[1]);
    assertEquals(2, region.presentCounts()[4]);
  }

  @Test
  void testFarmParsing() {
    var farm = ChristmasTreeFarm.parse(EXAMPLE_INPUT);
    assertEquals(6, farm.shapes().length);
    assertEquals(5, farm.regions().length);
  }

  @Test
  void testPlacementSolverAreaCheck() {
    var shapes = new Shape[] { new Shape(7, 3) };
    var solver = new PlacementSolver(shapes);

    // 4x4 = 16 cells, 2 shapes of 7 = 14 cells -> fits
    var region1 = new Region(4, 4, new int[] { 2 });
    assertTrue(solver.canFit(region1));

    // 3x4 = 12 cells, 2 shapes of 7 = 14 cells -> doesn't fit
    var region2 = new Region(3, 4, new int[] { 2 });
    assertFalse(solver.canFit(region2));
  }

  @Test
  void testPlacementSolverDimensionCheck() {
    // Shape with minDimension 3
    var shapes = new Shape[] { new Shape(7, 3) };
    var solver = new PlacementSolver(shapes);

    // 3x10 = 30 cells, 1 shape of 7 cells, min dim 3 fits in 3 -> fits
    var region1 = new Region(3, 10, new int[] { 1 });
    assertTrue(solver.canFit(region1));

    // 2x20 = 40 cells, 1 shape of 7 cells, min dim 3 doesn't fit in 2 -> doesn't fit
    var region2 = new Region(2, 20, new int[] { 1 });
    assertFalse(solver.canFit(region2));
  }

  @Test
  void testExampleCountFittingRegions() {
    var farm = ChristmasTreeFarm.parse(EXAMPLE_INPUT);
    // 4x4 with 2 shapes: fits (16 >= 14)
    // 5x5 with 2 shapes: fits (25 >= 14)
    // 10x10 with 0 shapes: fits (trivially)
    // 3x3 with 1 shape: fits (9 >= 7, and 3x3 shape fits)
    // 2x2 with 1 shape: doesn't fit (4 < 7)
    assertEquals(4, farm.countFittingRegions());
  }

  @Test
  void testZeroShapes() {
    var shapes = new Shape[] { new Shape(7, 3) };
    var solver = new PlacementSolver(shapes);

    // Region with 0 of each shape -> always fits
    var region = new Region(1, 1, new int[] { 0 });
    assertTrue(solver.canFit(region));
  }

  @Test
  void testMultipleShapeTypes() {
    var shapes = new Shape[] {
        new Shape(7, 3),  // shape 0: 7 cells, min dim 3
        new Shape(5, 2)   // shape 1: 5 cells, min dim 2
    };
    var solver = new PlacementSolver(shapes);

    // 5x5 = 25 cells, 1 of shape 0 (7) + 2 of shape 1 (10) = 17 cells -> fits
    var region1 = new Region(5, 5, new int[] { 1, 2 });
    assertTrue(solver.canFit(region1));

    // 4x4 = 16 cells, 1 of shape 0 (7) + 2 of shape 1 (10) = 17 cells -> doesn't fit
    var region2 = new Region(4, 4, new int[] { 1, 2 });
    assertFalse(solver.canFit(region2));
  }
}
