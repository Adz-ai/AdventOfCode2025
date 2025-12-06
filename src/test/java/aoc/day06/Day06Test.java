package aoc.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class Day06Test {

  private static final List<String> EXAMPLE_INPUT = List.of(
      "123 328  51 64 ",
      " 45 64  387 23 ",
      "  6 98  215 314",
      "*   +   *   +  "
  );

  @Test
  void testPart1WithExampleData() {
    var worksheet = MathWorksheet.parse(EXAMPLE_INPUT);
    // 123*45*6=33210, 328+64+98=490, 51*387*215=4243455, 64+23+314=401
    // Total: 33210+490+4243455+401=4277556
    assertEquals(4_277_556L, worksheet.solveAndSum());
  }

  @Test
  void testSingleAddition() {
    var input = List.of(
        "10",
        "20",
        "+ "
    );
    var worksheet = MathWorksheet.parse(input);
    assertEquals(30L, worksheet.solveAndSum());
  }

  @Test
  void testSingleMultiplication() {
    var input = List.of(
        "10",
        "20",
        "* "
    );
    var worksheet = MathWorksheet.parse(input);
    assertEquals(200L, worksheet.solveAndSum());
  }

  @Test
  void testMathProblemAddition() {
    var problem = new MathProblem(List.of(1L, 2L, 3L), '+');
    assertEquals(6L, problem.solve());
  }

  @Test
  void testMathProblemMultiplication() {
    var problem = new MathProblem(List.of(2L, 3L, 4L), '*');
    assertEquals(24L, problem.solve());
  }

  @Test
  void testPart2WithExampleData() {
    var worksheet = MathWorksheet.parseCephalopod(EXAMPLE_INPUT);
    // Rightmost: 4+431+623=1058, 2nd: 175*581*32=3253600,
    // 3rd: 8+248+369=625, Leftmost: 356*24*1=8544
    // Total: 1058+3253600+625+8544=3263827
    assertEquals(3_263_827L, worksheet.solveAndSum());
  }
}
