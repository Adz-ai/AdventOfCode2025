package aoc.day10;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

/**
 * Integer Linear Programming solver using OJALGO.
 * Minimizes sum of non-negative integer variables subject to linear equality constraints.
 */
final class IlpSolver {

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

  long solveMinSum() {
    if (numVariables == 0) {
      return allTargetsZero() ? 0 : Long.MAX_VALUE;
    }

    ExpressionsBasedModel model = new ExpressionsBasedModel();

    // Create non-negative integer variables
    Variable[] variables = createVariables(model);

    // Add equality constraints
    addConstraints(model, variables);

    // Minimize sum of all variables
    configureObjective(variables);

    // Solve
    Optimisation.Result result = model.minimise();

    if (result.getState().isOptimal() || result.getState().isFeasible()) {
      long total = 0;
      for (int v = 0; v < numVariables; v++) {
        total += Math.round(result.get(v).doubleValue());
      }
      return total;
    }

    return Long.MAX_VALUE;
  }

  private Variable @NotNull [] createVariables(@NotNull ExpressionsBasedModel model) {
    Variable[] variables = new Variable[numVariables];
    for (int v = 0; v < numVariables; v++) {
      variables[v] = model.newVariable("x" + v)
          .lower(0)
          .integer(true);
    }
    return variables;
  }

  private void addConstraints(@NotNull ExpressionsBasedModel model, Variable[] variables) {
    for (int c = 0; c < numConstraints; c++) {
      Expression constraint = model.newExpression("c" + c).level(targets[c]);
      for (int v = 0; v < numVariables; v++) {
        if (coefficients[c][v] != 0) {
          constraint.set(variables[v], coefficients[c][v]);
        }
      }
    }
  }

  private void configureObjective(Variable @NotNull [] variables) {
    for (Variable variable : variables) {
      variable.weight(1);
    }
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
