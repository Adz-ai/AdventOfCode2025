package aoc.day07;

import aoc.util.FileUtils;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day07 {

  private static final Logger LOG = LoggerFactory.getLogger(Day07.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day07/input");
      var manifold = TachyonManifold.parse(lines);
      LOG.info("Part 1: {}", manifold.countSplits());
      LOG.info("Part 2: {}", manifold.countTimelines());
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
