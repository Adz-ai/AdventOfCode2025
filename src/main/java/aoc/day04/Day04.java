package aoc.day04;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day04 {

  private static final Logger LOG = LoggerFactory.getLogger(Day04.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day04/input");
      SolutionRunner.run(LOG, () -> new Results(
          new PaperRollGrid(lines).countAccessibleRolls(),
          new PaperRollGrid(lines).countTotalRemovableRolls()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
