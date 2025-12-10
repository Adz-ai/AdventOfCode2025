package aoc.day10;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

final class Factory {

  private static final Pattern MACHINE_PATTERN =
      Pattern.compile("\\[([.#]+)]\\s+((?:\\([^)]+\\)\\s*)+)\\{([^}]+)}");
  private static final Pattern BUTTON_PATTERN = Pattern.compile("\\(([^)]+)\\)");
  private static final char LIGHT_ON = '#';

  private final List<Machine> machines;

  private Factory(List<Machine> machines) {
    this.machines = machines;
  }

  static @NotNull Factory parse(@NotNull List<String> lines) {
    List<Machine> machines = new ArrayList<>();
    for (String line : lines) {
      if (!line.isBlank()) {
        machines.add(parseMachine(line));
      }
    }
    return new Factory(machines);
  }

  private static @NotNull Machine parseMachine(String line) {
    Matcher matcher = MACHINE_PATTERN.matcher(line);
    if (!matcher.find()) {
      throw new IllegalArgumentException("Invalid machine format: " + line);
    }

    String lightDiagram = matcher.group(1);
    int numLights = lightDiagram.length();
    int targetState = parseTargetState(lightDiagram);

    String buttonSection = matcher.group(2);
    List<List<Integer>> buttonIndices = new ArrayList<>();
    Matcher buttonMatcher = BUTTON_PATTERN.matcher(buttonSection);
    while (buttonMatcher.find()) {
      buttonIndices.add(parseButtonIndices(buttonMatcher.group(1)));
    }

    String joltageSection = matcher.group(3);
    int[] joltageTargets = parseJoltageTargets(joltageSection);

    return new Machine(numLights, targetState, buttonIndices, joltageTargets);
  }

  private static int parseTargetState(@NotNull String diagram) {
    int state = 0;
    for (int i = 0; i < diagram.length(); i++) {
      if (diagram.charAt(i) == LIGHT_ON) {
        state |= (1 << i);
      }
    }
    return state;
  }

  private static @NotNull List<Integer> parseButtonIndices(@NotNull String buttonStr) {
    List<Integer> indices = new ArrayList<>();
    if (!buttonStr.isBlank()) {
      for (String idx : buttonStr.split(",")) {
        indices.add(Integer.parseInt(idx.trim()));
      }
    }
    return indices;
  }

  private static int @NotNull [] parseJoltageTargets(@NotNull String joltageStr) {
    String[] parts = joltageStr.split(",");
    int[] targets = new int[parts.length];
    for (int i = 0; i < parts.length; i++) {
      targets[i] = Integer.parseInt(parts[i].trim());
    }
    return targets;
  }

  long minTotalButtonPresses() {
    return machines.parallelStream()
        .mapToLong(Machine::minButtonPressesPartOne)
        .sum();
  }

  long minTotalJoltagePresses() {
    return machines.parallelStream()
        .mapToLong(Machine::minButtonPressesPartTwo)
        .sum();
  }

  private record Machine(int numLights, int targetState,
                         List<List<Integer>> buttonIndices, int[] joltageTargets) {

    int minButtonPressesPartOne() {
      int numButtons = buttonIndices.size();
      if (numButtons == 0) {
        return targetState == 0 ? 0 : -1;
      }

      int[] buttonMasks = buildButtonMasks(numLights);
      return findMinPresses(buttonMasks, numButtons);
    }

    private int @NotNull [] buildButtonMasks(int maxIndex) {
      int[] masks = new int[buttonIndices.size()];
      for (int b = 0; b < buttonIndices.size(); b++) {
        int mask = 0;
        for (int idx : buttonIndices.get(b)) {
          if (idx < maxIndex) {
            mask |= (1 << idx);
          }
        }
        masks[b] = mask;
      }
      return masks;
    }

    private int findMinPresses(int[] buttonMasks, int numButtons) {
      int minPresses = Integer.MAX_VALUE;

      for (int mask = 0; mask < (1 << numButtons); mask++) {
        int state = computeState(buttonMasks, mask, numButtons);
        if (state == targetState) {
          int presses = Integer.bitCount(mask);
          minPresses = Math.min(minPresses, presses);
        }
      }

      return minPresses == Integer.MAX_VALUE ? -1 : minPresses;
    }

    private int computeState(int[] buttonMasks, int selectionMask, int numButtons) {
      int state = 0;
      for (int b = 0; b < numButtons; b++) {
        if ((selectionMask & (1 << b)) != 0) {
          state ^= buttonMasks[b];
        }
      }
      return state;
    }

    long minButtonPressesPartTwo() {
      int numCounters = joltageTargets.length;
      int numButtons = buttonIndices.size();

      // Build coefficient matrix: coefficients[counter][button] = 1 if button affects counter
      // For Part 2, button indices refer to counter indices
      int[][] coefficients = new int[numCounters][numButtons];
      for (int b = 0; b < numButtons; b++) {
        for (int idx : buttonIndices.get(b)) {
          if (idx < numCounters) {
            coefficients[idx][b] = 1;
          }
        }
      }

      IlpSolver solver = IlpSolver.create(coefficients, joltageTargets);
      return solver.solveMinSum();
    }
  }
}
