package plague_simulator.graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import plague_simulator.graph.IGraphNode;

import static plague_simulator.utils.RandomUtils.nextInt;

public class GraphUtils {
  // Generates graph by drawing edges, each with same probability, and calling addAdj on each node from the nodes list.
  // If E is less than or greater than possible number of edges, the value is rounded to the closest valid value.
  static public <T extends IGraphNode<? super T>> void generateRandomSimpleUndirectedGraph(List<? extends T> nodes, long E) {
    final int N = nodes.size();
    final long MAX_E = (long)N * (N - 1L) / 2L; // N choose 2

    // Edges to be included in the graph
    Set<SortedIntegerPair> edges = new HashSet<>();

    // This way it's always O(E).
    if (E <= MAX_E / 2L) {
      while (edges.size() < E) {
        edges.add(generateRandomSortedIntegerPair(N));
      }
    } else {
      for (int i = 0 ; i < N ; i += 1) for (int j = i + 1 ; j < N ; j += 1) {
        edges.add(SortedIntegerPair.createWithoutCheck(i, j));
      }

      // Works if E > MAX_E
      while (edges.size() > E) {
        edges.remove(generateRandomSortedIntegerPair(N));
      }
    }

    for (SortedIntegerPair e : edges) {
      T v = nodes.get(e.getFirst());
      T u = nodes.get(e.getSecond());
      v.addAdj(u);
      u.addAdj(v);
    }
  }


  static private int generateRandomInt(int to) {
    return nextInt(to);
  }

  static private SortedIntegerPair generateRandomSortedIntegerPair(int to) {
    final int first = generateRandomInt(to);
    int second;
    do {
      second = generateRandomInt(to);
    } while (second == first);

    return new SortedIntegerPair(first, second);
  }
}

// Integer pair maintaining the order of elements
@Data
@NoArgsConstructor(access = AccessLevel.NONE)
final class SortedIntegerPair implements Comparable<SortedIntegerPair> {
  private final int first;
  private final int second;

  // Helper method, see ::createWithoutCheck
  private SortedIntegerPair(Void v, int first, int second) {
    this.first = first;
    this.second = second;
  }

  public SortedIntegerPair(int a, int b) {
    if (a <= b) {
      first = a;
      second = b;
    } else {
      first = b;
      second = a;
    }
  }

  // Assumes arguments are already sorted
  static public SortedIntegerPair createWithoutCheck(int first, int second) {
    return new SortedIntegerPair(null, first, second);
  }


  static private final Comparator<SortedIntegerPair> COMPARATOR =
      Comparator.comparing(SortedIntegerPair::getFirst).thenComparing(SortedIntegerPair::getSecond);

  @Override
  public int compareTo(SortedIntegerPair o) {
    return COMPARATOR.compare(this, o);
  }

  static public int compare(SortedIntegerPair lhs, SortedIntegerPair rhs) {
    if (lhs == null && rhs == null) { return 0; }
    return lhs.compareTo(rhs);
  }
}
