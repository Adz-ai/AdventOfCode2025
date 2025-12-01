package aoc.day01;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {

    private static final List<String> EXAMPLE_INPUT = List.of(
            "L68", "L30", "R48", "L5", "R60", "L55", "L1", "L99", "R14", "L82"
    );

    @Test
    void testPart1WithExampleData() {
        var day01 = new Day01();
        var lock = day01.runSimulation(EXAMPLE_INPUT);

        assertEquals(3, lock.getEndOfRotationZeroCount());
    }

    @Test
    void testPart2WithExampleData() {
        var day01 = new Day01();
        var lock = day01.runSimulation(EXAMPLE_INPUT);

        assertEquals(6, lock.getZeroHitCount());
    }

    @Test
    void testDialWrapsClockwise() {
        var lock = new SafeLock();
        lock.turnClockwise(60);

        assertEquals(1, lock.getZeroHitCount());
    }

    @Test
    void testDialWrapsCounterClockwise() {
        var lock = new SafeLock();
        lock.turnCounterClockwise(60);

        assertEquals(1, lock.getZeroHitCount());
    }

    @Test
    void testMultipleRotations() {
        var lock = new SafeLock();
        lock.turnClockwise(1000);

        assertEquals(10, lock.getZeroHitCount());
        assertEquals(0, lock.getEndOfRotationZeroCount());
    }

    @Test
    void testStartingAtZeroDoesNotCountImmediately() {
        var lock = new SafeLock();
        lock.turnClockwise(50);
        lock.turnCounterClockwise(1);
        
        assertEquals(1, lock.getZeroHitCount());
    }
}
