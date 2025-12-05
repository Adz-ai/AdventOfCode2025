package aoc.day05;

import org.jetbrains.annotations.NotNull;

record FreshRange(long start, long end) {

  static @NotNull FreshRange parse(@NotNull String line) {
    var parts = line.split("-");
    return new FreshRange(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
  }

  boolean contains(long ingredientId) {
    return ingredientId >= start && ingredientId <= end;
  }

  boolean overlapsOrAdjacent(@NotNull FreshRange other) {
    return other.start <= this.end + 1;
  }

  @NotNull FreshRange mergeWith(@NotNull FreshRange other) {
    return new FreshRange(this.start, Math.max(this.end, other.end));
  }

  long size() {
    return end - start + 1;
  }
}
