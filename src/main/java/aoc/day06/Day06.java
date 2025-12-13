package aoc.day06;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day06 {

  private static final Logger LOG = LoggerFactory.getLogger(Day06.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day06/input");
      SolutionRunner.run(LOG, () -> new Results(
          MathWorksheet.parse(lines).solveAndSum(),
          MathWorksheet.parseCephalopod(lines).solveAndSum()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
