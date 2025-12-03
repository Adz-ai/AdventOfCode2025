package aoc.day01;

/**
 * Represents a safe lock dial with 100 positions (0-99).
 *
 * <p>The dial starts at position 50 and can be turned clockwise or counter-clockwise.
 * Tracks zero crossings during rotation and landings on position zero.
 */
public class SafeLock {
  private static final int DIAL_SIZE = 100;
  private static final int STARTING_POSITION = 50;

  private int position = STARTING_POSITION;
  private int zeroHitCount;
  private int endOfRotationZeroCount;

  /**
   * Gets the total number of times the dial crossed position zero during any rotation.
   *
   * @return the zero crossing count
   */
  public int getZeroHitCount() {
    return zeroHitCount;
  }

  /**
   * Gets the count of rotations that ended exactly on position zero.
   *
   * @return the end-of-rotation zero count
   */
  public int getEndOfRotationZeroCount() {
    return endOfRotationZeroCount;
  }

  /**
   * Turns the dial clockwise by the specified number of steps.
   *
   * @param steps  the number of steps to rotate, not negative
   */
  public void turnClockwise(int steps) {
    turn(steps);
  }

  /**
   * Turns the dial counter-clockwise by the specified number of steps.
   *
   * @param steps  the number of steps to rotate, not negative
   */
  public void turnCounterClockwise(int steps) {
    turn(-steps);
  }

  private void turn(int steps) {
    if (steps == 0) {
      return;
    }

    zeroHitCount += countZeroCrossings(steps);
    position = Math.floorMod(position + steps, DIAL_SIZE);

    if (position == 0) {
      endOfRotationZeroCount++;
    }
  }

  private int countZeroCrossings(int steps) {
    int stepsToFirstZero = calculateStepsToZero(steps > 0);
    int absSteps = Math.abs(steps);

    if (absSteps < stepsToFirstZero) {
      return 0;
    }

    int remainingSteps = absSteps - stepsToFirstZero;
    return 1 + remainingSteps / DIAL_SIZE;
  }

  private int calculateStepsToZero(boolean clockwise) {
    if (position == 0) {
      return DIAL_SIZE;
    }
    return clockwise ? DIAL_SIZE - position : position;
  }
}
