package aoc.day09;

import aoc.util.FileUtils;
import aoc.util.SolutionRunner;
import aoc.util.SolutionRunner.Results;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day09 {

  private static final Logger LOG = LoggerFactory.getLogger(Day09.class);

  void main() {
    try {
      var lines = FileUtils.readLines("day09/input");
      var theater = MovieTheater.parse(lines);
      SolutionRunner.run(LOG, () -> new Results(
          theater.largestRectangleArea(),
          theater.largestValidRectangleArea()
      ));
    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }
}
