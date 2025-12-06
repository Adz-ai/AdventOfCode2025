package aoc.day06;

import java.util.List;

record MathProblem(List<Long> numbers, char operator) {

  long solve() {
    return switch (operator) {
      case '+' -> numbers.stream().mapToLong(Long::longValue).sum();
      case '*' -> numbers.stream().mapToLong(Long::longValue).reduce(1L, (a, b) -> a * b);
      default -> throw new IllegalArgumentException("Unknown operator: " + operator);
    };
  }
}
