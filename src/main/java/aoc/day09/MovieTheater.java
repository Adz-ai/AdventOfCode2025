package aoc.day09;

import java.util.List;
import org.jetbrains.annotations.NotNull;

final class MovieTheater {

  private final int[] xcoords;
  private final int[] ycoords;
  private RectilinearPolygon polygon;

  private MovieTheater(int[] xcoords, int[] ycoords) {
    this.xcoords = xcoords;
    this.ycoords = ycoords;
  }

  static @NotNull MovieTheater parse(@NotNull List<String> lines) {
    var filtered = lines.stream()
        .filter(line -> !line.isBlank())
        .toList();

    int n = filtered.size();
    int[] xcoords = new int[n];
    int[] ycoords = new int[n];

    for (int i = 0; i < n; i++) {
      var parts = filtered.get(i).split(",");
      xcoords[i] = Integer.parseInt(parts[0].trim());
      ycoords[i] = Integer.parseInt(parts[1].trim());
    }

    return new MovieTheater(xcoords, ycoords);
  }

  long largestRectangleArea() {
    int n = xcoords.length;
    long maxArea = 0;

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        long area = computeArea(i, j);
        if (area > maxArea) {
          maxArea = area;
        }
      }
    }

    return maxArea;
  }

  long largestValidRectangleArea() {
    polygon = new RectilinearPolygon(xcoords, ycoords);

    int n = xcoords.length;
    long maxArea = 0;

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        long area = computeValidArea(i, j);
        if (area > maxArea) {
          maxArea = area;
        }
      }
    }

    return maxArea;
  }

  private long computeValidArea(int i, int j) {
    int x1 = xcoords[i];
    int y1 = ycoords[i];
    int x2 = xcoords[j];
    int y2 = ycoords[j];

    if (x1 == x2 || y1 == y2) {
      return 0;
    }

    if (!polygon.containsPoint(x1, y2) || !polygon.containsPoint(x2, y1)) {
      return 0;
    }

    int minX = Math.min(x1, x2);
    int maxX = Math.max(x1, x2);
    int minY = Math.min(y1, y2);
    int maxY = Math.max(y1, y2);

    if (!polygon.containsRectangle(minX, maxX, minY, maxY)) {
      return 0;
    }

    return computeArea(i, j);
  }

  private long computeArea(int i, int j) {
    long width = Math.abs(xcoords[j] - xcoords[i]) + 1L;
    long height = Math.abs(ycoords[j] - ycoords[i]) + 1L;
    return width * height;
  }

  boolean testContainsPoint(int x, int y) {
    if (polygon == null) {
      polygon = new RectilinearPolygon(xcoords, ycoords);
    }
    return polygon.containsPoint(x, y);
  }
}
