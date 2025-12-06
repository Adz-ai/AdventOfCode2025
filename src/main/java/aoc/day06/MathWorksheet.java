package aoc.day06;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

record MathWorksheet(List<MathProblem> problems) {

  MathWorksheet(@NotNull List<MathProblem> problems) {
    this.problems = List.copyOf(problems);
  }

  static @NotNull MathWorksheet parse(@NotNull List<String> lines) {
    return parseInternal(lines, false);
  }

  static @NotNull MathWorksheet parseCephalopod(@NotNull List<String> lines) {
    return parseInternal(lines, true);
  }

  private static @NotNull MathWorksheet parseInternal(
      @NotNull List<String> lines, boolean cephalopodMode) {
    if (lines.isEmpty()) {
      return new MathWorksheet(List.of());
    }

    // Filter out empty lines
    var nonEmptyLines = lines.stream()
        .filter(line -> !line.isBlank())
        .toList();

    if (nonEmptyLines.isEmpty()) {
      return new MathWorksheet(List.of());
    }

    var operatorRow = nonEmptyLines.getLast();
    var numberRows = nonEmptyLines.subList(0, nonEmptyLines.size() - 1);

    // Find problem column boundaries based on all-space columns as separators
    var columnBoundaries = findColumnBoundaries(nonEmptyLines);
    var problems = new ArrayList<MathProblem>();

    for (int[] boundary : columnBoundaries) {
      int start = boundary[0];
      int end = boundary[1];

      char operator = findOperator(operatorRow, start, end);
      var numbers = cephalopodMode
          ? extractVerticalNumbers(numberRows, start, end)
          : extractNumbers(numberRows, start, end);

      if (!numbers.isEmpty()) {
        problems.add(new MathProblem(numbers, operator));
      }
    }

    return new MathWorksheet(problems);
  }

  private static @NotNull List<int[]> findColumnBoundaries(@NotNull List<String> lines) {
    int maxLen = lines.stream().mapToInt(String::length).max().orElse(0);

    var boundaries = new ArrayList<int[]>();
    int col = 0;

    while (col < maxLen) {
      // Skip separator columns (all-space columns)
      while (col < maxLen && isAllSpaces(lines, col)) {
        col++;
      }

      if (col >= maxLen) {
        break;
      }

      // Find the end of this problem (next all-space column)
      int start = col;
      while (col < maxLen && !isAllSpaces(lines, col)) {
        col++;
      }

      if (col > start) {
        boundaries.add(new int[]{start, col});
      }
    }

    return boundaries;
  }

  private static boolean isAllSpaces(@NotNull List<String> lines, int col) {
    for (var line : lines) {
      if (col < line.length() && line.charAt(col) != ' ') {
        return false;
      }
    }
    return true;
  }

  private static char findOperator(@NotNull String operatorRow, int start, int end) {
    for (int i = start; i < end && i < operatorRow.length(); i++) {
      char ch = operatorRow.charAt(i);
      if (ch == '+' || ch == '*') {
        return ch;
      }
    }
    throw new IllegalArgumentException("No operator found in range " + start + "-" + end);
  }

  private static @NotNull List<Long> extractNumbers(
      @NotNull List<String> rows, int startCol, int endCol) {
    var numbers = new ArrayList<Long>();

    for (var row : rows) {
      if (startCol >= row.length()) {
        continue;
      }

      int actualEnd = Math.min(endCol, row.length());
      var segment = row.substring(startCol, actualEnd).trim();

      if (!segment.isEmpty()) {
        numbers.add(Long.parseLong(segment));
      }
    }

    return numbers;
  }

  private static @NotNull List<Long> extractVerticalNumbers(
      @NotNull List<String> rows, int startCol, int endCol) {
    var numbers = new ArrayList<Long>();

    // Each column within the boundary is a separate number (digits read top-to-bottom)
    for (int col = startCol; col < endCol; col++) {
      long number = extractVerticalNumber(rows, col);
      if (number >= 0) {
        numbers.add(number);
      }
    }

    return numbers;
  }

  private static long extractVerticalNumber(@NotNull List<String> rows, int col) {
    long result = 0;
    boolean hasDigit = false;

    for (var row : rows) {
      if (col < row.length()) {
        char ch = row.charAt(col);
        if (Character.isDigit(ch)) {
          result = result * 10 + ch - '0';
          hasDigit = true;
        }
      }
    }

    return hasDigit ? result : -1;
  }

  long solveAndSum() {
    return problems.stream()
        .mapToLong(MathProblem::solve)
        .sum();
  }
}
