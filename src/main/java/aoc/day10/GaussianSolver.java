package aoc.day10;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Gaussian elimination solver with recursive enumeration for free variables.
 * Solves systems of linear equations over non-negative integers, minimizing the sum of variables.
 */
final class GaussianSolver {

  private static final double EPS = 1e-8;

  private final int numVars;
  private final int numConstraints;

  // Variable expressions: varCoeffs[v][i] * x[i] + varConstants[v] = x[v]
  private final double[][] varCoeffs;
  private final double[] varConstants;
  private final boolean[] isFree;
  private final int[] upperBounds;

  private GaussianSolver(int numVars, int numConstraints, int[] upperBounds) {
    this.numVars = numVars;
    this.numConstraints = numConstraints;
    this.upperBounds = upperBounds;
    this.varCoeffs = new double[numVars][numVars];
    this.varConstants = new double[numVars];
    this.isFree = new boolean[numVars];

    // Initialize each variable as free: x[i] = 1*x[i] + 0
    for (int i = 0; i < numVars; i++) {
      varCoeffs[i][i] = 1.0;
      isFree[i] = true;
    }
  }

  static long solve(int[][] coefficients, int @NotNull [] targets) {
    int numConstraints = targets.length;
    if (numConstraints == 0 || coefficients.length == 0 || coefficients[0].length == 0) {
      return allZero(targets) ? 0 : Long.MAX_VALUE;
    }

    int numVars = coefficients[0].length;
    var upperBounds = computeUpperBounds(coefficients, targets, numVars);
    var solver = new GaussianSolver(numVars, numConstraints, upperBounds);

    // Build equations and perform elimination
    var eqCoeffs = new double[numConstraints][numVars];
    var eqConstants = new double[numConstraints];

    for (int c = 0; c < numConstraints; c++) {
      eqConstants[c] = -targets[c];
      for (int v = 0; v < numVars; v++) {
        eqCoeffs[c][v] = coefficients[c][v];
      }
    }

    solver.performElimination(eqCoeffs, eqConstants);
    return solver.findMinimum();
  }

  private static int @NotNull [] computeUpperBounds(int[][] coefficients, int @NotNull [] targets, int numVars) {
    var bounds = new int[numVars];
    Arrays.fill(bounds, Integer.MAX_VALUE);
    for (int c = 0; c < targets.length; c++) {
      for (int v = 0; v < numVars; v++) {
        if (coefficients[c][v] != 0) {
          bounds[v] = Math.min(bounds[v], targets[c]);
        }
      }
    }
    return bounds;
  }

  @Contract(pure = true)
  private static boolean allZero(int @NotNull [] arr) {
    for (int val : arr) {
      if (val != 0) return false;
    }
    return true;
  }

  private void performElimination(double[][] eqCoeffs, double[] eqConstants) {
    for (int v = 0; v < numVars; v++) {
      for (int e = 0; e < numConstraints; e++) {
        double pivot = -eqCoeffs[e][v];
        if (Math.abs(pivot) < EPS) continue;

        // Extract: x[v] = (constant + sum(coeff[i]*x[i] for i!=v)) / pivot
        isFree[v] = false;
        varConstants[v] = eqConstants[e] / pivot;
        for (int i = 0; i < numVars; i++) {
          varCoeffs[v][i] = (i == v) ? 0 : eqCoeffs[e][i] / pivot;
        }

        // Substitute into all equations
        for (int j = 0; j < numConstraints; j++) {
          double coeff = eqCoeffs[j][v];
          if (Math.abs(coeff) < EPS) continue;

          eqCoeffs[j][v] = 0;
          for (int i = 0; i < numVars; i++) {
            eqCoeffs[j][i] += coeff * varCoeffs[v][i];
          }
          eqConstants[j] += coeff * varConstants[v];
        }
        break;
      }
    }
  }

  private long findMinimum() {
    // Collect free variable indices
    int freeCount = 0;
    for (int i = 0; i < numVars; i++) {
      if (isFree[i]) freeCount++;
    }

    var freeVars = new int[freeCount];
    int idx = 0;
    for (int i = 0; i < numVars; i++) {
      if (isFree[i]) freeVars[idx++] = i;
    }

    return enumerate(freeVars, 0, new int[numVars], Long.MAX_VALUE);
  }

  private long enumerate(int @NotNull [] freeVars, int depth, int[] values, long bestSoFar) {
    if (depth == freeVars.length) {
      return evaluate(values);
    }

    int varIdx = freeVars[depth];
    int bound = upperBounds[varIdx];
    long best = bestSoFar;

    for (int x = 0; x <= bound; x++) {
      values[varIdx] = x;
      long result = enumerate(freeVars, depth + 1, values, best);
      if (result < best) {
        best = result;
      }
    }
    return best;
  }

  private long evaluate(int[] values) {
    long total = 0;

    // Evaluate variables in reverse order (dependent vars may reference later free vars)
    for (int i = numVars - 1; i >= 0; i--) {
      double x;
      if (isFree[i]) {
        x = values[i];
      } else {
        x = varConstants[i];
        var coeffs = varCoeffs[i];
        for (int j = 0; j < numVars; j++) {
          x += coeffs[j] * values[j];
        }
      }

      // Validate: must be non-negative integer
      if (x < -EPS) return Long.MAX_VALUE;
      long rounded = Math.round(x);
      if (Math.abs(x - rounded) > EPS) return Long.MAX_VALUE;

      values[i] = (int) rounded;
      total += rounded;
    }
    return total;
  }
}
