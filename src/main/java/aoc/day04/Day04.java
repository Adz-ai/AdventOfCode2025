package aoc.day04;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day04 {

  private static final Logger LOG = LoggerFactory.getLogger(Day04.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day04/input");
        log.info("Part 1: {}", new PaperRollGrid(lines).countAccessibleRolls());
        log.info("Part 2: {}", new PaperRollGrid(lines).countTotalRemovableRolls());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
