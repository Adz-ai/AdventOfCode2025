package aoc.day07;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day07 {

  private static final Logger LOG = LoggerFactory.getLogger(Day07.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day07/input");
      var manifold = TachyonManifold.parse(lines);
      SolutionRunner.run(LOG, () -> new Results(
          manifold.countSplits(),
          manifold.countTimelines()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
