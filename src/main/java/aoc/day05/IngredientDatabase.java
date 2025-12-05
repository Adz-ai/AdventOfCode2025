package aoc.day05;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Gatherer;
import org.jetbrains.annotations.NotNull;

public record IngredientDatabase(
    @NotNull List<FreshRange> freshRanges,
    @NotNull List<Long> availableIngredients) {

  public IngredientDatabase {
    freshRanges = List.copyOf(freshRanges);
    availableIngredients = List.copyOf(availableIngredients);
  }

  public static @NotNull IngredientDatabase parse(@NotNull List<List<String>> sections) {
    var ranges = sections.getFirst().stream()
        .map(FreshRange::parse)
        .toList();

    var ingredients = sections.get(1).stream()
        .map(Long::parseLong)
        .toList();

    return new IngredientDatabase(ranges, ingredients);
  }

  public long countFreshIngredients() {
    return availableIngredients.stream()
        .filter(this::isFresh)
        .count();
  }

  public long countTotalFreshIds() {
    return freshRanges.stream()
        .sorted(Comparator.comparingLong(FreshRange::start))
        .gather(mergeOverlappingRanges())
        .mapToLong(FreshRange::size)
        .sum();
  }

  private static @NotNull Gatherer<FreshRange, ?, FreshRange> mergeOverlappingRanges() {
    return Gatherer.ofSequential(
        AtomicReference<FreshRange>::new,
        (state, next, downstream) -> {
          var current = state.get();
          if (current == null) {
            state.set(next);
          } else if (current.overlapsOrAdjacent(next)) {
            state.set(current.mergeWith(next));
          } else {
            downstream.push(current);
            state.set(next);
          }
          return true;
        },
        (state, downstream) -> {
          var remaining = state.get();
          if (remaining != null) {
            downstream.push(remaining);
          }
        }
    );
  }

  private boolean isFresh(long ingredientId) {
    return freshRanges.stream()
        .anyMatch(range -> range.contains(ingredientId));
  }
}
