package aoc.day01;

public class SafeLock {
    private static final int TOTAL_POSITIONS = 100;

    private int currentPosition;
    private int zeroHitCount;
    private int endOfRotationZeroCount;

    public SafeLock() {
        this.currentPosition = 50;
        this.zeroHitCount = 0;
        this.endOfRotationZeroCount = 0;
    }

    public int getZeroHitCount() {
        return zeroHitCount;
    }

    public int getEndOfRotationZeroCount() {
        return endOfRotationZeroCount;
    }

    private int calculateZeroHits(int startPos, int steps) {
        if (steps == 0) return 0;
        int hits = 0;
        if (steps > 0) {
            int stepsToFirstZero = (startPos == 0) ? TOTAL_POSITIONS : (TOTAL_POSITIONS - startPos);
            if (steps >= stepsToFirstZero) {
                hits++;
                int remainingSteps = steps - stepsToFirstZero;
                hits += remainingSteps / TOTAL_POSITIONS;
            }
        } else {
            int absSteps = Math.abs(steps);
            int stepsToFirstZero = (startPos == 0) ? TOTAL_POSITIONS : startPos;
            if (absSteps >= stepsToFirstZero) {
                hits++;
                int remainingSteps = absSteps - stepsToFirstZero;
                hits += remainingSteps / TOTAL_POSITIONS;
            }
        }
        return hits;
    }

    public void turnClockwise(int steps) {
        if (steps < 0) throw new IllegalArgumentException("Steps must be non-negative.");
        this.zeroHitCount += calculateZeroHits(this.currentPosition, steps);
        long rawNewPosition = (long) this.currentPosition + steps;
        this.currentPosition = (int) (rawNewPosition % TOTAL_POSITIONS);
        if (this.currentPosition == 0) this.endOfRotationZeroCount++;
    }

    public void turnCounterClockwise(int steps) {
        if (steps < 0) throw new IllegalArgumentException("Steps must be non-negative.");
        this.zeroHitCount += calculateZeroHits(this.currentPosition, -steps);
        int rawNewPosition = this.currentPosition - steps;
        this.currentPosition = (rawNewPosition % TOTAL_POSITIONS + TOTAL_POSITIONS) % TOTAL_POSITIONS;
        if (this.currentPosition == 0) this.endOfRotationZeroCount++;
    }
}