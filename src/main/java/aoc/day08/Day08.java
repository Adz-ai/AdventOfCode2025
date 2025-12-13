package aoc.day08;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day08 {

  private static final Logger LOG = LoggerFactory.getLogger(Day08.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day08/input");
      var playground = Playground.parse(lines);
      SolutionRunner.run(LOG, () -> new Results(
          playground.productOfThreeLargestCircuits(1000),
          playground.finalConnectionXcoordProduct()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
