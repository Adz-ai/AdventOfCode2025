package aoc.day10;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day10 {

  private static final Logger LOG = LoggerFactory.getLogger(Day10.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day10/input.txt");
      var factory = Factory.parse(lines);
      SolutionRunner.run(LOG, () -> new Results(
          factory.minTotalButtonPresses(),
          factory.minTotalJoltagePresses()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
