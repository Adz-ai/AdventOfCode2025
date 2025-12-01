package aoc.util;

public class MathUtils {

  public static long gcd(long a, long b) {
    long absA = Math.abs(a);
    long absB = Math.abs(b);
    while (absB != 0) {
      long temp = absB;
      absB = absA % absB;
      absA = temp;
    }
    return absA;
  }

  public static long lcm(long a, long b) {
    return Math.abs(a * b) / gcd(a, b);
  }
}
