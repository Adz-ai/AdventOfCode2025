package aoc.day02;

import java.util.stream.LongStream;

public record Range(long start, long end) {
  static Range parse(String s) {
    var parts = s.trim().split("-");
    return new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
  }

  LongStream stream() {
    return LongStream.rangeClosed(start, end);
  }
}
