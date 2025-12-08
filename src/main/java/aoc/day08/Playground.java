package aoc.day08;

import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

final class Playground {

  private final int[] xcoords;
  private final int[] ycoords;
  private final int[] zcoords;
  private long[] sortedPairData;

  @SuppressWarnings("PMD.UseVarargs")
  private Playground(int[] xcoords, int[] ycoords, int[] zcoords) {
    this.xcoords = xcoords;
    this.ycoords = ycoords;
    this.zcoords = zcoords;
  }

  static @NotNull Playground parse(@NotNull List<String> lines) {
    var filtered = lines.stream()
        .filter(line -> !line.isBlank())
        .toList();

    int n = filtered.size();
    int[] xcoords = new int[n];
    int[] ycoords = new int[n];
    int[] zcoords = new int[n];

    for (int i = 0; i < n; i++) {
      var parts = filtered.get(i).split(",");
      xcoords[i] = Integer.parseInt(parts[0].trim());
      ycoords[i] = Integer.parseInt(parts[1].trim());
      zcoords[i] = Integer.parseInt(parts[2].trim());
    }

    return new Playground(xcoords, ycoords, zcoords);
  }

  private long[] getSortedPairData() {
    if (sortedPairData == null) {
      int n = xcoords.length;
      int pairCount = n * (n - 1) / 2;

      sortedPairData = new long[pairCount];
      int idx = 0;

      for (int i = 0; i < n; i++) {
        int xi = xcoords[i];
        int yi = ycoords[i];
        int zi = zcoords[i];
        for (int j = i + 1; j < n; j++) {
          long dx = (long) xi - xcoords[j];
          long dy = (long) yi - ycoords[j];
          long dz = (long) zi - zcoords[j];
          long distSq = dx * dx + dy * dy + dz * dz;
          // Pack: high bits = distance, low 10 bits each = i, j
          sortedPairData[idx] = (distSq << 20) | ((long) i << 10) | j;
          idx++;
        }
      }

      Arrays.parallelSort(sortedPairData);
    }
    return sortedPairData;
  }

  private static int extractBoxA(long packed) {
    return (int) ((packed >> 10) & 0x3FF);
  }

  private static int extractBoxB(long packed) {
    return (int) (packed & 0x3FF);
  }

  long productOfThreeLargestCircuits(int connections) {
    var uf = new UnionFind(xcoords.length);
    var pairs = getSortedPairData();

    int limit = Math.min(connections, pairs.length);
    for (int i = 0; i < limit; i++) {
      uf.union(extractBoxA(pairs[i]), extractBoxB(pairs[i]));
    }

    var sizes = uf.getComponentSizes();
    sizes.sort((a, b) -> Integer.compare(b, a));

    long product = 1;
    int count = Math.min(3, sizes.size());
    for (int i = 0; i < count; i++) {
      product *= sizes.get(i);
    }
    return product;
  }

  long finalConnectionXcoordProduct() {
    var uf = new UnionFind(xcoords.length);
    var pairs = getSortedPairData();

    int lastA = -1;
    int lastB = -1;

    for (long pair : pairs) {
      int boxA = extractBoxA(pair);
      int boxB = extractBoxB(pair);

      if (uf.find(boxA) != uf.find(boxB)) {
        uf.union(boxA, boxB);
        lastA = boxA;
        lastB = boxB;

        if (uf.isFullyConnected()) {
          break;
        }
      }
    }

    if (lastA < 0) {
      return 0;
    }

    return (long) xcoords[lastA] * xcoords[lastB];
  }
}
