package aoc.day12;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a region under a tree with presents to place.
 */
record Region(int width, int height, int[] presentCounts) {

  /**
   * Parse a region line like "12x5: 1 0 1 0 2 2".
   */
  @Contract("_ -> new")
  static @NotNull Region parse(@NotNull String line) {
    int colonIndex = line.indexOf(':');
    int separatorIndex = line.indexOf('x');

    int width = Integer.parseInt(line.substring(0, separatorIndex));
    int height = Integer.parseInt(line.substring(separatorIndex + 1, colonIndex));

    String[] countParts = line.substring(colonIndex + 1).trim().split(" ");
    int[] counts = new int[countParts.length];
    for (int i = 0; i < countParts.length; i++) {
      counts[i] = Integer.parseInt(countParts[i]);
    }

    return new Region(width, height, counts);
  }

  int area() {
    return width * height;
  }

  int minDimension() {
    return Math.min(width, height);
  }
}
