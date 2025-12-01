package aoc.day01;

public class SafeLock {
    private static final int DIAL_SIZE = 100;
    private static final int STARTING_POSITION = 50;

    private int position = STARTING_POSITION;
    private int zeroHitCount;
    private int endOfRotationZeroCount;

    public int getZeroHitCount() {
        return zeroHitCount;
    }

    public int getEndOfRotationZeroCount() {
        return endOfRotationZeroCount;
    }

    public void turnClockwise(int steps) {
        turn(steps);
    }

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
