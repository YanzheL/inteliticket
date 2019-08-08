package org.yanzhe.inteliticket.core.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

public class SimpleAdjacentListGraph<ET extends GeneralEdge> extends AbstractAdjacentListGraph<ET> {

  protected Set<ET>[] uniqAdj;

  public SimpleAdjacentListGraph() {
    super();
  }

  public SimpleAdjacentListGraph(@NotNull AbstractAdjacentListGraph<ET> graph) {
    super(graph);
  }

  @Override
  public Collection<ET>[] getAdj() {
    return uniqAdj;
  }

  @Override
  protected boolean initStorage(int V) {
    this.V = V;
    uniqAdj = (TreeSet<ET>[]) new TreeSet[V];
    for (int v = 0; v < V; ++v) {
      uniqAdj[v] = new TreeSet<>(Comparator.comparingInt(ET::w));
    }
    return true;
  }

  @NotNull
  @Override
  public List<ET> getEdge(int v, int w) {
    for (ET edge : uniqAdj[v]) {
      if (edge.w() == w) {
        return Collections.singletonList(edge);
      }
    }
    return Collections.emptyList();
  }
}
