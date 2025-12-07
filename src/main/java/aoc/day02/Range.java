package aoc.day02;

import java.util.stream.LongStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

record Range(long start, long end) {
  @Contract("_ -> new")
  static @NotNull Range parse(@NotNull String s) {
    var parts = s.trim().split("-");
    return new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
  }

  @Contract(pure = true)
  @NotNull LongStream stream() {
    return LongStream.rangeClosed(start, end);
  }
}
