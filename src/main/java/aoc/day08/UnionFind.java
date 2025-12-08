package aoc.day08;

import java.util.ArrayList;
import java.util.List;

class UnionFind {

  private final int[] parent;
  private final int[] rank;
  private int componentCount;

  UnionFind(int n) {
    parent = new int[n];
    rank = new int[n];
    componentCount = n;
    for (int i = 0; i < n; i++) {
      parent[i] = i;
      rank[i] = 0;
    }
  }

  int find(int x) {
    if (parent[x] != x) {
      parent[x] = find(parent[x]); // Path compression
    }
    return parent[x];
  }

  void union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX != rootY) {
      componentCount--;
      // Union by rank
      if (rank[rootX] < rank[rootY]) {
        parent[rootX] = rootY;
      } else if (rank[rootX] > rank[rootY]) {
        parent[rootY] = rootX;
      } else {
        parent[rootY] = rootX;
        rank[rootX]++;
      }
    }
  }

  boolean isFullyConnected() {
    return componentCount == 1;
  }

  List<Integer> getComponentSizes() {
    int[] sizes = new int[parent.length];
    for (int i = 0; i < parent.length; i++) {
      sizes[find(i)]++;
    }

    var result = new ArrayList<Integer>();
    for (int size : sizes) {
      if (size > 0) {
        result.add(size);
      }
    }
    return result;
  }
}
