package org.yanzhe.inteliticket.core.graph;

import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public class DefaultAdjacentListGraph<ET extends GeneralEdge> extends
    AbstractAdjacentListGraph<ET> {

  private ArrayList<ET>[] adj;

  public DefaultAdjacentListGraph() {
    super();
  }

  public DefaultAdjacentListGraph(@NotNull AbstractAdjacentListGraph<ET> graph) {
    super(graph);
  }

  public Collection<ET>[] getAdj() {
    return adj;
  }

  protected boolean initStorage(int V) {
    this.V = V;
    adj = (ArrayList<ET>[]) new ArrayList[V];
    for (int v = 0; v < V; ++v) {
      adj[v] = new ArrayList<>();
    }
    return true;
  }

  // 获取图的总顶点数

//  @SuppressWarnings("unchecked")
//  public DefaultAdjacentListGraph<ET> transpose() {
//    DefaultAdjacentListGraph<ET> tG = new DefaultAdjacentListGraph<>();
//    tG.init(V);
//    for (ET edge : edges()) {
//      ET reversE = (ET) edge.reverse(false);
//      tG.addEdge(reversE);
//    }
//    return tG;
//  }
}
