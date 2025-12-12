package aoc.day12;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day12 {

  private static final Logger LOG = LoggerFactory.getLogger(Day12.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day12/input.txt");
        var farm = ChristmasTreeFarm.parse(lines);
        log.info("Part 1: {}", farm.countFittingRegions());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
