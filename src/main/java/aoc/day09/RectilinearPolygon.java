package aoc.day09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a rectilinear (axis-aligned) polygon and provides efficient
 * point-in-polygon and rectangle containment tests.
 */
final class RectilinearPolygon {

  private final NavigableMap<Integer, List<Range>> verticalEdgesByX = new TreeMap<>();
  private final NavigableMap<Integer, List<Range>> horizontalEdgesByY = new TreeMap<>();
  private final Map<Long, Boolean> containsPointCache = new HashMap<>();

  RectilinearPolygon(int @NotNull [] xcoords, int[] ycoords) {
    int n = xcoords.length;
    for (int i = 0; i < n; i++) {
      int next = (i + 1) % n;
      addEdge(xcoords[i], ycoords[i], xcoords[next], ycoords[next]);
    }
  }

  private void addEdge(int x1, int y1, int x2, int y2) {
    if (x1 == x2) {
      verticalEdgesByX.computeIfAbsent(x1, _ -> new ArrayList<>())
          .add(Range.of(y1, y2));
    } else {
      horizontalEdgesByY.computeIfAbsent(y1, _ -> new ArrayList<>())
          .add(Range.of(x1, x2));
    }
  }

  boolean containsPoint(int x, int y) {
    long key = ((long) x << 32) | (y & 0xFFFFFFFFL);
    return containsPointCache.computeIfAbsent(key, _ -> computeContainsPoint(x, y));
  }

  private boolean computeContainsPoint(int x, int y) {
    return isOnEdge(x, y) || hasOddCrossingsToRight(x, y);
  }

  private boolean isOnEdge(int x, int y) {
    return pointInRangeList(horizontalEdgesByY.get(y), x)
        || pointInRangeList(verticalEdgesByX.get(x), y);
  }

  private boolean pointInRangeList(List<Range> ranges, int value) {
    if (ranges == null) {
      return false;
    }
    for (var range : ranges) {
      if (range.contains(value)) {
        return true;
      }
    }
    return false;
  }

  private boolean hasOddCrossingsToRight(int x, int y) {
    int crossings = 0;
    for (var entry : verticalEdgesByX.tailMap(x, false).entrySet()) {
      for (var edge : entry.getValue()) {
        if (y > edge.min && y <= edge.max) {
          crossings++;
        }
      }
    }
    return (crossings & 1) == 1;
  }

  boolean containsRectangle(int minX, int maxX, int minY, int maxY) {
    return hasEdgeCrossingInterior(verticalEdgesByX, minX, maxX, minY, maxY)
        && hasEdgeCrossingInterior(horizontalEdgesByY, minY, maxY, minX, maxX);
  }

  private boolean hasEdgeCrossingInterior(
          @NotNull NavigableMap<Integer, List<Range>> edgeMap,
          int keyMin, int keyMax, int rangeMin, int rangeMax) {
    for (var edges : edgeMap.subMap(keyMin, false, keyMax, false).values()) {
      for (var edge : edges) {
        if (edge.overlaps(rangeMin, rangeMax)) {
          return false;
        }
      }
    }
    return true;
  }

  private record Range(int min, int max) {
    @Contract("_, _ -> new")
    static @NotNull Range of(int a, int b) {
      return new Range(Math.min(a, b), Math.max(a, b));
    }

    boolean contains(int value) {
      return value >= min && value <= max;
    }

    boolean overlaps(int otherMin, int otherMax) {
      return min < otherMax && max > otherMin;
    }
  }
}
