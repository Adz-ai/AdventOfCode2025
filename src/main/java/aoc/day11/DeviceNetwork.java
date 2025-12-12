package aoc.day11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a network of devices connected via directed edges.
 * Provides efficient path counting from a source node to a destination node.
 */
record DeviceNetwork(Map<String, List<String>> adjacencyList) {

  private static final String PART1_START = "you";
  private static final String PART2_START = "svr";
  private static final String END_NODE = "out";
  private static final String CHECKPOINT_DAC = "dac";
  private static final String CHECKPOINT_FFT = "fft";

  /**
   * Parses input lines into a DeviceNetwork.
   * Each line format: "deviceName: connection1 connection2 ..."
   */
  @Contract("_ -> new")
  static @NotNull DeviceNetwork parse(@NotNull List<String> lines) {
    var adjacencyList = new HashMap<String, List<String>>();

    for (var line : lines) {
      var parts = line.split(": ");
      var device = parts[0];
      var connections = parts.length > 1
          ? Arrays.asList(parts[1].split(" "))
          : List.<String>of();
      adjacencyList.put(device, connections);
    }

    return new DeviceNetwork(adjacencyList);
  }

  /**
   * Part 1: Counts all distinct paths from "you" to "out".
   */
  long countPathsToOutput() {
    return countPaths(PART1_START, END_NODE, new HashMap<>());
  }

  /**
   * Part 2: Counts paths from "svr" to "out" that pass through both "dac" and "fft".
   * Since paths must visit both checkpoints, we count:
   * - paths: svr -> dac -> fft -> out
   * - paths: svr -> fft -> dac -> out
   */
  long countPathsThroughCheckpoints() {
    // Path: svr -> dac -> fft -> out
    long dacFirst = countPaths(PART2_START, CHECKPOINT_DAC, new HashMap<>())
        * countPaths(CHECKPOINT_DAC, CHECKPOINT_FFT, new HashMap<>())
        * countPaths(CHECKPOINT_FFT, END_NODE, new HashMap<>());

    // Path: svr -> fft -> dac -> out
    long fftFirst = countPaths(PART2_START, CHECKPOINT_FFT, new HashMap<>())
        * countPaths(CHECKPOINT_FFT, CHECKPOINT_DAC, new HashMap<>())
        * countPaths(CHECKPOINT_DAC, END_NODE, new HashMap<>());

    return dacFirst + fftFirst;
  }

  private long countPaths(@NotNull String from, String to, Map<String, Long> memo) {
    if (from.equals(to)) {
      return 1L;
    }

    if (memo.containsKey(from)) {
      return memo.get(from);
    }

    var connections = adjacencyList.get(from);
    if (connections == null || connections.isEmpty()) {
      memo.put(from, 0L);
      return 0L;
    }

    long pathCount = 0L;
    for (var neighbor : connections) {
      pathCount += countPaths(neighbor, to, memo);
    }

    memo.put(from, pathCount);
    return pathCount;
  }
}
