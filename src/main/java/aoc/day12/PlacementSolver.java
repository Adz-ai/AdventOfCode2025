package aoc.day12;

import org.jetbrains.annotations.NotNull;

/**
 * Solves the present placement problem.
 *
 * <p>For a rectangle, polyominoes can always fit if:
 * 1. Total area of shapes <= area of region
 * 2. Each shape can physically fit in the dimensions
 */
final class PlacementSolver {

  private final int[] shapeSizes;
  private final int[] shapeMinDimensions;

  PlacementSolver(Shape @NotNull [] shapes) {
    this.shapeSizes = new int[shapes.length];
    this.shapeMinDimensions = new int[shapes.length];

    for (int i = 0; i < shapes.length; i++) {
      shapeSizes[i] = shapes[i].size();
      shapeMinDimensions[i] = shapes[i].minDimension();
    }
  }

  boolean canFit(@NotNull Region region) {
    int[] counts = region.presentCounts();
    int regionArea = region.area();
    int regionMinDim = region.minDimension();

    int requiredArea = 0;
    for (int i = 0; i < counts.length; i++) {
      int count = counts[i];
      if (count > 0) {
        requiredArea += count * shapeSizes[i];

        // Check shape fits in region dimensions
        if (shapeMinDimensions[i] > regionMinDim) {
          return false;
        }
      }
    }

    return requiredArea <= regionArea;
  }
}
