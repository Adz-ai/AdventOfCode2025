package aoc.day06;

import aoc.util.FileUtils;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day06 {

  private static final Logger LOG = LoggerFactory.getLogger(Day06.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day06/input");
      var worksheet1 = MathWorksheet.parse(lines);
      LOG.info("Part 1: {}", worksheet1.solveAndSum());

      var worksheet2 = MathWorksheet.parseCephalopod(lines);
      LOG.info("Part 2: {}", worksheet2.solveAndSum());
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
