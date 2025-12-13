package aoc.day05;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
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
      SolutionRunner.run(LOG, () -> new Results(
          database.countFreshIngredients(),
          database.countTotalFreshIds()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
