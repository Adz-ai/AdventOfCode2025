package aoc.day09;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day09 {

  private static final Logger LOG = LoggerFactory.getLogger(Day09.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day09/input");
        var theater = MovieTheater.parse(lines);
        log.info("Part 1: {}", theater.largestRectangleArea());
        log.info("Part 2: {}", theater.largestValidRectangleArea());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
