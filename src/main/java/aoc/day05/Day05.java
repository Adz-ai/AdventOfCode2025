package aoc.day05;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day05 {

  private static final Logger LOG = LoggerFactory.getLogger(Day05.class);

  void main() {
    SolutionRunner.run(LOG, log -> {
      try {
        var lines = FileUtils.readLines("day05/input");
        var sections = FileUtils.splitByBlankLines(lines);
        var database = IngredientDatabase.parse(sections);
        log.info("Part 1: {}", database.countFreshIngredients());
        log.info("Part 2: {}", database.countTotalFreshIds());
      } catch (IOException e) {
        log.error("Error reading input", e);
      }
    });
  }
}
