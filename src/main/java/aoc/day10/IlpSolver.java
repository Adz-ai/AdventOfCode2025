package aoc.day10;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Integer Linear Programming solver using Google OR-Tools.
 * Minimizes sum of non-negative integer variables subject to linear equality constraints.
 */
final class IlpSolver {

  private static final double INFINITY = Double.POSITIVE_INFINITY;
  private static volatile boolean nativeLoaded;

  private final int[][] coefficients;
  private final int[] targets;
  private final int numVariables;
  private final int numConstraints;

  @Contract(pure = true)
  private IlpSolver(int[] @NotNull [] coefficients, int @NotNull [] targets) {
    this.coefficients = coefficients;
    this.targets = targets;
    this.numConstraints = targets.length;
    this.numVariables = coefficients.length > 0 ? coefficients[0].length : 0;
  }

  @Contract("_, _ -> new")
  static @NotNull IlpSolver create(int[] @NotNull [] coefficients, int @NotNull [] targets) {
    return new IlpSolver(coefficients, targets);
  }

  private static synchronized void ensureNativeLoaded() {
    if (!nativeLoaded) {
      Loader.loadNativeLibraries();
      nativeLoaded = true;
    }
  }

  long solveMinSum() {
    if (numVariables == 0) {
      return allTargetsZero() ? 0 : Long.MAX_VALUE;
    }

    ensureNativeLoaded();
    MPSolver solver = createSolver();
    MPVariable[] variables = createVariables(solver);
    addConstraints(solver, variables);
    configureObjective(solver, variables);

    return solve(solver, variables);
  }

  private @NotNull MPSolver createSolver() {
    MPSolver solver = MPSolver.createSolver("SCIP");
    if (solver == null) {
      solver = MPSolver.createSolver("CBC");
    }
    if (solver == null) {
      throw new IllegalStateException("Could not create ILP solver");
    }
    return solver;
  }

  private MPVariable @NotNull [] createVariables(@NotNull MPSolver solver) {
    MPVariable[] variables = new MPVariable[numVariables];
    for (int v = 0; v < numVariables; v++) {
      variables[v] = solver.makeIntVar(0, INFINITY, "x" + v);
    }
    return variables;
  }

  private void addConstraints(@NotNull MPSolver solver, MPVariable[] variables) {
    for (int c = 0; c < numConstraints; c++) {
      MPConstraint constraint = solver.makeConstraint(targets[c], targets[c], "c" + c);
      for (int v = 0; v < numVariables; v++) {
        if (coefficients[c][v] != 0) {
          constraint.setCoefficient(variables[v], coefficients[c][v]);
        }
      }
    }
  }

  private void configureObjective(@NotNull MPSolver solver, MPVariable[] variables) {
    MPObjective objective = solver.objective();
    for (int v = 0; v < numVariables; v++) {
      objective.setCoefficient(variables[v], 1);
    }
    objective.setMinimization();
  }

  private long solve(@NotNull MPSolver solver, MPVariable[] variables) {
    MPSolver.ResultStatus status = solver.solve();

    if (status == MPSolver.ResultStatus.OPTIMAL || status == MPSolver.ResultStatus.FEASIBLE) {
      long total = 0;
      for (int v = 0; v < numVariables; v++) {
        total += Math.round(variables[v].solutionValue());
      }
      return total;
    }

    return Long.MAX_VALUE;
  }

  private boolean allTargetsZero() {
    for (int t : targets) {
      if (t != 0) {
        return false;
      }
    }
    return true;
  }
}
