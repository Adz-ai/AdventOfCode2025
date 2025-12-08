package aoc.day08;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day08 {

  private static final Logger LOG = LoggerFactory.getLogger(Day08.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day08/input");
        var playground = Playground.parse(lines);
        log.info("Part 1: {}", playground.productOfThreeLargestCircuits(1000));
        log.info("Part 2: {}", playground.finalConnectionXcoordProduct());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
