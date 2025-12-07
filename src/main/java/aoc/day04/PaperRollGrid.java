package aoc.day04;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;


final class PaperRollGrid {

  private static final char PAPER_ROLL = '@';
  private static final char EMPTY = '.';
  private static final int MAX_ADJACENT_FOR_ACCESS = 4;

  private final char[][] grid;
  private final int rows;
  private final int cols;

  PaperRollGrid(@NotNull List<String> lines) {
    this.rows = lines.size();
    this.cols = lines.isEmpty() ? 0 : lines.getFirst().length();
    this.grid = parseGrid(lines);
  }

  private static char[] @NotNull [] parseGrid(@NotNull List<String> lines) {
    var result = new char[lines.size()][];
    for (int row = 0; row < lines.size(); row++) {
      result[row] = lines.get(row).toCharArray();
    }
    return result;
  }

  long countAccessibleRolls() {
    long count = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (isPaperRoll(row, col) && isAccessible(row, col)) {
          count++;
        }
      }
    }
    return count;
  }

  long countTotalRemovableRolls() {
    long totalRemoved = 0;

    var accessibleRolls = findAccessibleRolls();
    while (!accessibleRolls.isEmpty()) {
      totalRemoved += accessibleRolls.size();
      removeRolls(accessibleRolls);
      accessibleRolls = findAccessibleRolls();
    }

    return totalRemoved;
  }

  private @NotNull List<Position> findAccessibleRolls() {
    var accessible = new ArrayList<Position>();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (isPaperRoll(row, col) && isAccessible(row, col)) {
          accessible.add(new Position(row, col));
        }
      }
    }
    return accessible;
  }

  private void removeRolls(@NotNull List<Position> rolls) {
    for (var pos : rolls) {
      grid[pos.row()][pos.col()] = EMPTY;
    }
  }

  private boolean isPaperRoll(int row, int col) {
    return grid[row][col] == PAPER_ROLL;
  }

  private boolean isAccessible(int row, int col) {
    return countAdjacentPaperRolls(row, col) < MAX_ADJACENT_FOR_ACCESS;
  }

  private int countAdjacentPaperRolls(int row, int col) {
    int count = 0;
    for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
      for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
        if (deltaRow == 0 && deltaCol == 0) {
          continue;
        }

        int newRow = row + deltaRow;
        int newCol = col + deltaCol;

        if (isWithinBounds(newRow, newCol) && isPaperRoll(newRow, newCol)) {
          count++;
        }
      }
    }
    return count;
  }

  private boolean isWithinBounds(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  private record Position(int row, int col) {
  }
}
