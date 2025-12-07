package aoc.day07;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day07 {

  private static final Logger LOG = LoggerFactory.getLogger(Day07.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day07/input");
        var manifold = TachyonManifold.parse(lines);
        log.info("Part 1: {}", manifold.countSplits());
        log.info("Part 2: {}", manifold.countTimelines());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
