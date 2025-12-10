package aoc.day10;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day10 {

  private static final Logger LOG = LoggerFactory.getLogger(Day10.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day10/input.txt");
        var factory = Factory.parse(lines);
        log.info("Part 1: {}", factory.minTotalButtonPresses());
        log.info("Part 2: {}", factory.minTotalJoltagePresses());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
