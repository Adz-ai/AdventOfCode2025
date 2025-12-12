package aoc.day10;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Gaussian elimination solver with recursive enumeration for free variables.
 * Solves systems of linear equations over non-negative integers, minimizing the sum of variables.
 */
final class GaussianSolver {

  private static final double EPS = 1e-8;

  private final int numVariables;
  private final int numConstraints;
  private final double[][] coefficients;
  private final double[] targets;
  private final int[] upperBounds;

  @Contract(pure = true)
  private GaussianSolver(double[] @NotNull [] coefficients, double @NotNull [] targets, int[] upperBounds) {
    this.coefficients = coefficients;
    this.targets = targets;
    this.numConstraints = targets.length;
    this.numVariables = coefficients.length > 0 ? coefficients[0].length : 0;
    this.upperBounds = upperBounds;
  }

  @Contract("_, _ -> new")
  static @NotNull GaussianSolver create(int[][] intCoefficients, int @NotNull [] intTargets) {
    int numConstraints = intTargets.length;
    int numVariables = numConstraints > 0 && intCoefficients.length > 0
        ? intCoefficients[0].length : 0;

    var coefficients = new double[numConstraints][numVariables];
    var targets = new double[numConstraints];
    var upperBounds = new int[numVariables];
    Arrays.fill(upperBounds, Integer.MAX_VALUE);

    for (int c = 0; c < numConstraints; c++) {
      targets[c] = intTargets[c];
      for (int v = 0; v < numVariables; v++) {
        coefficients[c][v] = intCoefficients[c][v];
        if (intCoefficients[c][v] != 0) {
          upperBounds[v] = Math.min(upperBounds[v], intTargets[c]);
        }
      }
    }

    return new GaussianSolver(coefficients, targets, upperBounds);
  }

  long solveMinSum() {
    if (numVariables == 0) {
      return allTargetsZero() ? 0 : Long.MAX_VALUE;
    }

    var variables = initializeVariables();
    var equations = buildEquations();
    performGaussianElimination(variables, equations);

    int[] freeVarIndices = findFreeVariables(variables);
    return enumerateSolutions(variables, freeVarIndices);
  }

  private Variable @NotNull [] initializeVariables() {
    var variables = new Variable[numVariables];
    for (int i = 0; i < numVariables; i++) {
      variables[i] = Variable.free(i, numVariables, upperBounds[i]);
    }
    return variables;
  }

  private LinearExpr @NotNull [] buildEquations() {
    var equations = new LinearExpr[numConstraints];
    for (int c = 0; c < numConstraints; c++) {
      equations[c] = new LinearExpr(coefficients[c].clone(), -targets[c]);
    }
    return equations;
  }

  private void performGaussianElimination(Variable[] variables, LinearExpr[] equations) {
    for (int v = 0; v < numVariables; v++) {
      for (var equation : equations) {
        var extracted = equation.extractVariable(v);
        if (extracted != null) {
          variables[v] = Variable.dependent(extracted, upperBounds[v]);
          substituteInAllEquations(equations, v, extracted);
          break;
        }
      }
    }
  }

  private void substituteInAllEquations(LinearExpr @NotNull [] equations, int varIndex, LinearExpr expr) {
    for (int i = 0; i < equations.length; i++) {
      equations[i] = equations[i].substitute(varIndex, expr);
    }
  }

  private int[] findFreeVariables(Variable[] variables) {
    return java.util.stream.IntStream.range(0, numVariables)
        .filter(i -> variables[i].isFree())
        .toArray();
  }

  private long enumerateSolutions(Variable[] variables, int[] freeVarIndices) {
    return enumerate(variables, freeVarIndices, 0, new int[numVariables]);
  }

  private long enumerate(Variable[] variables, int @NotNull [] freeVars, int depth, int[] values) {
    if (depth == freeVars.length) {
      return evaluateSolution(variables, values);
    }

    int varIdx = freeVars[depth];
    long best = Long.MAX_VALUE;

    for (int x = 0; x <= variables[varIdx].upperBound(); x++) {
      values[varIdx] = x;
      best = Math.min(best, enumerate(variables, freeVars, depth + 1, values));
    }

    return best;
  }

  private long evaluateSolution(Variable @NotNull [] variables, int[] values) {
    long total = 0;
    for (int i = variables.length - 1; i >= 0; i--) {
      double x = variables[i].evaluate(values);
      if (!isValidNonNegativeInteger(x)) {
        return Long.MAX_VALUE;
      }
      values[i] = (int) Math.round(x);
      total += values[i];
    }
    return total;
  }

  private boolean isValidNonNegativeInteger(double x) {
    return x >= -EPS && Math.abs(x - Math.round(x)) <= EPS;
  }

  private boolean allTargetsZero() {
    return Arrays.stream(targets).allMatch(t -> Math.abs(t) <= EPS);
  }

  /**
   * Represents a linear expression: sum(coeffs[i] * x[i]) + constant.
   */
  private record LinearExpr(double[] coeffs, double constant) {

    LinearExpr {
      coeffs = coeffs.clone();
    }

    /**
     * Extracts variable v from this equation, expressing it in terms of other variables.
     * Returns null if coefficient of v is zero.
     */
    @Nullable LinearExpr extractVariable(int v) {
      double coeff = -coeffs[v];
      if (Math.abs(coeff) < EPS) {
        return null;
      }

      var newCoeffs = new double[coeffs.length];
      for (int i = 0; i < coeffs.length; i++) {
        if (i != v) {
          newCoeffs[i] = coeffs[i] / coeff;
        }
      }
      return new LinearExpr(newCoeffs, constant / coeff);
    }

    /**
     * Substitutes variable v with the given expression.
     */
    LinearExpr substitute(int v, LinearExpr expr) {
      double coeff = coeffs[v];
      if (Math.abs(coeff) < EPS) {
        return this;
      }

      var newCoeffs = coeffs.clone();
      newCoeffs[v] = 0;
      for (int i = 0; i < coeffs.length; i++) {
        newCoeffs[i] += coeff * expr.coeffs[i];
      }
      return new LinearExpr(newCoeffs, constant + coeff * expr.constant);
    }

    double evaluate(int[] values) {
      double result = constant;
      for (int i = 0; i < coeffs.length; i++) {
        result += coeffs[i] * values[i];
      }
      return result;
    }
  }

  /**
   * Represents a variable that is either free or expressed as a linear combination of free variables.
   */
  private record Variable(LinearExpr expr, boolean isFree, int upperBound) {

    @Contract("_, _, _ -> new")
    static @NotNull Variable free(int index, int numVars, int upperBound) {
      var coeffs = new double[numVars];
      coeffs[index] = 1;
      return new Variable(new LinearExpr(coeffs, 0), true, upperBound);
    }

    @Contract("_, _ -> new")
    static @NotNull Variable dependent(LinearExpr expr, int upperBound) {
      return new Variable(expr, false, upperBound);
    }

    double evaluate(int[] values) {
      return expr.evaluate(values);
    }
  }
}
