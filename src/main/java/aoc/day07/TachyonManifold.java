package aoc.day07;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

record TachyonManifold(char[][] grid, int startCol) {

  private static final char START = 'S';
  private static final char SPLITTER = '^';

  static @NotNull TachyonManifold parse(@NotNull List<String> lines) {
    if (lines.isEmpty()) {
      throw new IllegalArgumentException("Empty input");
    }

    int height = lines.size();
    int width = lines.getFirst().length();
    var grid = new char[height][width];
    int startCol = -1;

    for (int row = 0; row < height; row++) {
      var line = lines.get(row);
      for (int col = 0; col < width; col++) {
        char ch = col < line.length() ? line.charAt(col) : '.';
        grid[row][col] = ch;
        if (ch == START) {
          startCol = col;
        }
      }
    }

    if (startCol < 0) {
      throw new IllegalArgumentException("No start position 'S' found");
    }

    return new TachyonManifold(grid, startCol);
  }

  private int height() {
    return grid.length;
  }

  private int width() {
    return grid[0].length;
  }

  int countSplits() {
    var activatedSplitters = new HashSet<Long>();
    Deque<Long> beams = new ArrayDeque<>();

    int startRow = findStartRow();
    beams.add(encodePosition(startRow + 1, startCol));

    while (!beams.isEmpty()) {
      long beam = beams.poll();
      processBeam(decodeRow(beam), decodeCol(beam), activatedSplitters, beams);
    }

    return activatedSplitters.size();
  }

  private void processBeam(int startRow, int col, Set<Long> activated, Deque<Long> beams) {
    int splitterRow = findSplitterRow(startRow, col);
    if (splitterRow < 0) {
      return;
    }

    long splitterKey = encodePosition(splitterRow, col);
    if (activated.add(splitterKey)) {
      spawnNewBeams(splitterRow, col, beams);
    }
  }

  private int findSplitterRow(int startRow, int col) {
    for (int row = startRow; row < height(); row++) {
      if (grid[row][col] == SPLITTER) {
        return row;
      }
    }
    return -1;
  }

  private void spawnNewBeams(int row, int col, Deque<Long> beams) {
    int nextRow = row + 1;
    if (col > 0) {
      beams.add(encodePosition(nextRow, col - 1));
    }
    if (col + 1 < width()) {
      beams.add(encodePosition(nextRow, col + 1));
    }
  }

  long countTimelines() {
    int startRow = findStartRow();

    // Use two arrays for double buffering
    long[] current = new long[width()];
    long[] next = new long[width()];
    current[startCol] = 1;

    for (int row = startRow + 1; row < height(); row++) {
      java.util.Arrays.fill(next, 0);
      propagateTimelines(row, current, next);
      long[] temp = current;
      current = next;
      next = temp;
    }

    return java.util.Arrays.stream(current).sum();
  }

  private void propagateTimelines(int row, long[] current, long[] next) {
    int w = width();
    for (int col = 0; col < w; col++) {
      long count = current[col];
      if (count == 0) {
        continue;
      }

      if (grid[row][col] == SPLITTER) {
        if (col > 0) {
          next[col - 1] += count;
        }
        if (col + 1 < w) {
          next[col + 1] += count;
        }
      } else {
        next[col] += count;
      }
    }
  }

  private int findStartRow() {
    for (int row = 0; row < height(); row++) {
      if (grid[row][startCol] == START) {
        return row;
      }
    }
    throw new IllegalStateException("Start position not found");
  }

  private static long encodePosition(int row, int col) {
    return ((long) row << 32) | (col & 0xFFFFFFFFL);
  }

  private static int decodeRow(long encoded) {
    return (int) (encoded >> 32);
  }

  private static int decodeCol(long encoded) {
    return (int) encoded;
  }
}
