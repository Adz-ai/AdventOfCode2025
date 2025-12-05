package aoc.day05;

import aoc.util.FileUtils;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day05 {

  private static final Logger LOG = LoggerFactory.getLogger(Day05.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day05/input");
      var sections = FileUtils.splitByBlankLines(lines);
      var database = IngredientDatabase.parse(sections);
      LOG.info("Part 1: {}", database.countFreshIngredients());
      LOG.info("Part 2: {}", database.countTotalFreshIds());
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
