package aoc.day11;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day11 {

  private static final Logger LOG = LoggerFactory.getLogger(Day11.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day11/input.txt");
        var network = DeviceNetwork.parse(lines);
        log.info("Part 1: {}", network.countPathsToOutput());
        log.info("Part 2: {}", network.countPathsThroughCheckpoints());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
