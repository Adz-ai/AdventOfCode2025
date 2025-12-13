package aoc.day11;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day11 {

  private static final Logger LOG = LoggerFactory.getLogger(Day11.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day11/input.txt");
      var network = DeviceNetwork.parse(lines);
      SolutionRunner.run(LOG, () -> new Results(
          network.countPathsToOutput(),
          network.countPathsThroughCheckpoints()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
