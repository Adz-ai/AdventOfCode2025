package aoc.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day08Test {

  private static final List<String> EXAMPLE_INPUT = List.of(
      "162,817,812",
      "57,618,57",
      "906,360,560",
      "592,479,940",
      "352,342,300",
      "466,668,158",
      "542,29,236",
      "431,825,988",
      "739,650,466",
      "52,470,668",
      "216,146,977",
      "819,987,18",
      "117,168,530",
      "805,96,715",
      "346,949,466",
      "970,615,88",
      "941,993,340",
      "862,61,35",
      "984,92,344",
      "425,690,689"
  );

  @Test
  void testPart1WithExampleData() {
    var playground = Playground.parse(EXAMPLE_INPUT);
    // After 10 connections: 5 * 4 * 2 = 40
    assertEquals(40, playground.productOfThreeLargestCircuits(10));
  }

  @Test
  void testPart2WithExampleData() {
    var playground = Playground.parse(EXAMPLE_INPUT);
    // Last connection is between 216,146,977 and 117,168,530
    // Product of X coordinates: 216 * 117 = 25272
    assertEquals(25_272, playground.finalConnectionXcoordProduct());
  }
}
