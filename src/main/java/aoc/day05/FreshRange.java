package aoc.day05;

import org.jetbrains.annotations.NotNull;

public record FreshRange(long start, long end) {

  public static @NotNull FreshRange parse(@NotNull String line) {
    var parts = line.split("-");
    return new FreshRange(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
  }

  public boolean contains(long ingredientId) {
    return ingredientId >= start && ingredientId <= end;
  }

  public boolean overlapsOrAdjacent(@NotNull FreshRange other) {
    return other.start <= this.end + 1;
  }

  public @NotNull FreshRange mergeWith(@NotNull FreshRange other) {
    return new FreshRange(this.start, Math.max(this.end, other.end));
  }

  public long size() {
    return end - start + 1;
  }
}
