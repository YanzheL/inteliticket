package org.yanzhe.inteliticket.core.graph;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGraph<ET extends GeneralEdge> {

  protected int V;
  protected int E;
  protected boolean initialized = false;
  protected Set<Integer> removedNodes;

  public AbstractGraph() {
    removedNodes = new TreeSet<>();
  }

  public AbstractGraph(@NotNull AbstractGraph<ET> graph) {
    this();
    init(graph.V());
    for (ET edge : graph.edges()) {
      addEdge(edge);
    }
  }

  public abstract void addEdge(ET edge);

  @NotNull
  public abstract List<ET> edges();

  @NotNull
  public abstract List<ET> getEdge(int v, int w);

  public abstract void removeEdge(int from, int to);

  @NotNull
  public abstract List<ET> removeNode(int v);

  public abstract void clear();

  public abstract int degree(int v);

  protected abstract boolean initStorage(int V);

  public boolean init(int V) {
    initialized = initStorage(V);
    return initialized;
  }

  public int V() {
    return V;
  }

  // 获取图的总边数
  public int E() {
    return E;
  }

  public boolean isInitialized() {
    return initialized;
  }

  protected void validateVertex(int v) {
    if (v < 0 || v >= V) {
      throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
  }

  public void addEdges(@NotNull Iterable<ET> edges) {
    for (ET edge : edges) {
      addEdge(edge);
    }
  }

  public double avgDegree() {
    return 2.0 * E / V;
  }

  public boolean hasNode(int v) {
    return (v > 0 && v < V) && removedNodes.contains(v);
  }

  public int maxDegree() {
    int max = 0;
    for (int v = 0; v < V; ++v) {
      int deg = degree(v);
      if (deg > max) {
        max = deg;
      }
    }
    return max;
  }
}
