package aoc.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day11Test {

  private static final List<String> EXAMPLE_INPUT = List.of(
      "aaa: you hhh",
      "you: bbb ccc",
      "bbb: ddd eee",
      "ccc: ddd eee fff",
      "ddd: ggg",
      "eee: out",
      "fff: out",
      "ggg: out",
      "hhh: ccc fff iii",
      "iii: out"
  );

  @Test
  void testExamplePart1() {
    var network = DeviceNetwork.parse(EXAMPLE_INPUT);
    assertEquals(5, network.countPathsToOutput());
  }

  @Test
  void testSinglePath() {
    var input = List.of(
        "you: a",
        "a: b",
        "b: out"
    );
    var network = DeviceNetwork.parse(input);
    assertEquals(1, network.countPathsToOutput());
  }

  @Test
  void testDirectPath() {
    var input = List.of(
        "you: out"
    );
    var network = DeviceNetwork.parse(input);
    assertEquals(1, network.countPathsToOutput());
  }

  @Test
  void testMultiplePaths() {
    var input = List.of(
        "you: a b",
        "a: out",
        "b: out"
    );
    var network = DeviceNetwork.parse(input);
    assertEquals(2, network.countPathsToOutput());
  }

  @Test
  void testDiamondPattern() {
    var input = List.of(
        "you: a b",
        "a: c",
        "b: c",
        "c: out"
    );
    var network = DeviceNetwork.parse(input);
    assertEquals(2, network.countPathsToOutput());
  }

  @Test
  void testExamplePart2() {
    var input = List.of(
        "svr: aaa bbb",
        "aaa: fft",
        "fft: ccc",
        "bbb: tty",
        "tty: ccc",
        "ccc: ddd eee",
        "ddd: hub",
        "hub: fff",
        "eee: dac",
        "dac: fff",
        "fff: ggg hhh",
        "ggg: out",
        "hhh: out"
    );
    var network = DeviceNetwork.parse(input);
    assertEquals(2, network.countPathsThroughCheckpoints());
  }
}
