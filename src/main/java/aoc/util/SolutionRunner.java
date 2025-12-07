package aoc.util;

import java.time.Duration;
import java.time.Instant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public final class SolutionRunner {

  private static final long NANOS_PER_MICROSECOND = 1000;
  private static final long NANOS_PER_MILLISECOND = 1_000_000;
  private static final long MILLIS_PER_SECOND = 1000;
  private static final double MILLIS_TO_SECONDS = 1000.0;

  private SolutionRunner() {
  }

  public static void run(Logger log, @NotNull Solution solution) {
    var start = Instant.now();

    solution.solve(log);

    var elapsed = Duration.between(start, Instant.now());
    log.info("Completed in {}", formatDuration(elapsed));
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

  @FunctionalInterface
  public interface Solution {
    void solve(Logger log);
  }
}
