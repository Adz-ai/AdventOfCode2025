package aoc.day12;

import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a present shape with precomputed properties for efficient checking.
 *
 * @param size number of cells in the shape
 * @param minDimension smallest bounding box dimension across all orientations
 */
record Shape(int size, int minDimension) {

  private static final char SHAPE_CHAR = '#';

  /**
   * Parse a shape from lines and compute its properties.
   */
  @Contract("_ -> new")
  static @NotNull Shape parse(@NotNull List<String> lines) {
    int size = 0;
    int minRow = Integer.MAX_VALUE;
    int maxRow = Integer.MIN_VALUE;
    int minCol = Integer.MAX_VALUE;
    int maxCol = Integer.MIN_VALUE;

    for (int r = 0; r < lines.size(); r++) {
      String line = lines.get(r);
      for (int c = 0; c < line.length(); c++) {
        if (line.charAt(c) == SHAPE_CHAR) {
          size++;
          minRow = Math.min(minRow, r);
          maxRow = Math.max(maxRow, r);
          minCol = Math.min(minCol, c);
          maxCol = Math.max(maxCol, c);
        }
      }
    }

    if (size == 0) {
      return new Shape(0, 0);
    }

    int width = maxCol - minCol + 1;
    int height = maxRow - minRow + 1;

    // For all 8 orientations (4 rotations + flip + 4 rotations),
    // min dimension alternates between min(w,h) and stays the same after flip
    // So minDimension across all orientations is simply min(width, height)
    int minDim = Math.min(width, height);

    return new Shape(size, minDim);
  }
}
