package aoc.day12;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Christmas tree farm with shapes and regions to check.
 */
record ChristmasTreeFarm(Shape[] shapes, Region[] regions) {

  /**
   * Parse input into shapes and regions.
   */
  @Contract("_ -> new")
  static @NotNull ChristmasTreeFarm parse(List<String> lines) {
    List<Shape> shapeList = new ArrayList<>();
    List<Region> regionList = new ArrayList<>();

    int idx = parseShapes(lines, shapeList);
    parseRegions(lines, idx, regionList);

    return new ChristmasTreeFarm(
        shapeList.toArray(Shape[]::new),
        regionList.toArray(Region[]::new)
    );
  }

  private static int parseShapes(@NotNull List<String> lines, List<Shape> shapes) {
    int i = 0;
    while (i < lines.size()) {
      String line = lines.get(i);

      if (line.isBlank()) {
        i++;
        continue;
      }

      if (isRegionLine(line)) {
        break;
      }

      if (isShapeHeaderLine(line)) {
        i++;
        i = parseOneShape(lines, i, shapes);
      } else {
        i++;
      }
    }
    return i;
  }

  private static int parseOneShape(@NotNull List<String> lines, int startIdx, List<Shape> shapes) {
    List<String> shapeLines = new ArrayList<>();
    int i = startIdx;

    while (i < lines.size() && !lines.get(i).isBlank() && !lines.get(i).contains(":")) {
      shapeLines.add(lines.get(i));
      i++;
    }

    if (!shapeLines.isEmpty()) {
      shapes.add(Shape.parse(shapeLines));
    }
    return i;
  }

  private static void parseRegions(@NotNull List<String> lines, int startIdx,
                                    List<Region> regions) {
    for (int i = startIdx; i < lines.size(); i++) {
      String line = lines.get(i);
      if (isRegionLine(line)) {
        regions.add(Region.parse(line));
      }
    }
  }

  private static boolean isRegionLine(@NotNull String line) {
    return !line.isBlank() && line.contains("x") && line.contains(":")
        && Character.isDigit(line.charAt(0));
  }

  private static boolean isShapeHeaderLine(@NotNull String line) {
    return line.contains(":") && Character.isDigit(line.charAt(0));
  }

  /**
   * Count how many regions can fit all their listed presents.
   */
  long countFittingRegions() {
    var solver = new PlacementSolver(shapes);
    long count = 0;

    for (Region region : regions) {
      if (solver.canFit(region)) {
        count++;
      }
    }

    return count;
  }
}
