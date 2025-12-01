package aoc.day01;

import aoc.util.FileUtils;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day01 {

  private static final Logger LOG = LoggerFactory.getLogger(Day01.class);
  private static final char LEFT_DIRECTION = 'L';

  void main() {
    try {
      var lines = FileUtils.readLines("day01/input");
      var lock = runSimulation(lines);

      LOG.info("Part 1: {}", lock.getEndOfRotationZeroCount());
      LOG.info("Part 2: {}", lock.getZeroHitCount());

    } catch (IOException e) {
      LOG.error("Error reading input", e);
    }
  }

  SafeLock runSimulation(List<String> lines) {
    var lock = new SafeLock();
    for (var line : lines) {
      int amount = Integer.parseInt(line.substring(1));
      if (line.charAt(0) == LEFT_DIRECTION) {
        lock.turnCounterClockwise(amount);
      } else {
        lock.turnClockwise(amount);
      }
    }
    return lock;
  }
}
