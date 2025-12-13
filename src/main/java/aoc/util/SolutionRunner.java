package aoc.util;

import java.time.Duration;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Utility for running and timing Advent of Code solutions.
 */
public final class SolutionRunner {

  private static final long NANOS_PER_MICROSECOND = 1000;
  private static final long NANOS_PER_MILLISECOND = 1_000_000;
  private static final long MILLIS_PER_SECOND = 1000;
  private static final double MILLIS_TO_SECONDS = 1000.0;

  private SolutionRunner() {
  }

  /**
   * Runs and times a solution that returns results to be logged after timing.
   *
   * @param log the logger to use
   * @param solution supplier that computes and returns results
   */
  public static void run(@NotNull Logger log, @NotNull Supplier<Results> solution) {
    long start = System.nanoTime();
    var results = solution.get();
    long elapsed = System.nanoTime() - start;

    // Log results after timing
    log.info("Part 1: {}", results.part1());
    log.info("Part 2: {}", results.part2());
    log.info("Completed in {}", formatDuration(Duration.ofNanos(elapsed)));
  }

  /**
   * Runs and times a single-part solution.
   *
   * @param log the logger to use
   * @param solution supplier that computes and returns the result
   */
  public static void runSingle(@NotNull Logger log, @NotNull Supplier<Object> solution) {
    long start = System.nanoTime();
    var result = solution.get();
    long elapsed = System.nanoTime() - start;

    log.info("Result: {}", result);
    log.info("Completed in {}", formatDuration(Duration.ofNanos(elapsed)));
  }

  private static @NotNull String formatDuration(@NotNull Duration duration) {
    if (duration.toNanos() < NANOS_PER_MILLISECOND) {
      return "%d µs".formatted(duration.toNanos() / NANOS_PER_MICROSECOND);
    } else if (duration.toMillis() < MILLIS_PER_SECOND) {
      return "%d ms".formatted(duration.toMillis());
    } else {
      return "%.3f s".formatted(duration.toMillis() / MILLIS_TO_SECONDS);
    }
  }

  /**
   * Results from a two-part solution.
   */
  public record Results(Object part1, Object part2) {
  }
}
