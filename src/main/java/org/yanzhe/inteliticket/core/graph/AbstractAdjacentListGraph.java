package org.yanzhe.inteliticket.core.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAdjacentListGraph<ET extends GeneralEdge> extends AbstractGraph<ET> {

  public AbstractAdjacentListGraph() {
    super();
  }

  public AbstractAdjacentListGraph(@NotNull AbstractAdjacentListGraph<ET> graph) {
    super(graph);
  }

  public abstract Collection<ET>[] getAdj();

  // 将整个图转化为字符串的形式输出，便于屏幕打印图
  @NotNull
  @Override
  public String toString() {
    StringBuilder s =
        new StringBuilder(String.format("Total Vertexes = %d, Total Edges = %d\n", V, E));
    for (int v = 0; v < V; ++v) {
      s.append("Node ").append(v).append(": ");
      for (ET e : getAdj()[v]) {
        s.append(e).append("  ");
      }
      s.append("\n");
    }
    return s.toString();
  }

  @Override
  public void addEdge(@NotNull ET edge) {
    int v = edge.v(), w = edge.w();
    removedNodes.remove(v);
    removedNodes.remove(w);
    Collection<ET> list = getAdj()[v];
    if (!list.contains(edge)) {
      list.add(edge);
      ++E;
    }
  }

  @Override
  public int degree(int v) {
    validateVertex(v);
    return getAdj()[v].size();
  }

  @Override
  public void clear() {
    if (initialized) {
      V = 0;
      E = 0;
      for (Collection<ET> l : getAdj()) {
        l.clear();
      }
      initialized = false;
    }
  }

  // 返回顶点v的领接表
  public Iterable<ET> adj(int v) {
    validateVertex(v);
    return getAdj()[v];
  }

  @NotNull
  @Override
  public List<ET> edges() {
    List<ET> edges = new ArrayList<>();
    for (Collection<ET> i : getAdj()) {
      edges.addAll(i);
    }
    return edges;
  }

  @NotNull
  @Override
  public List<ET> getEdge(int v, int w) {
    List<ET> edges = new ArrayList<>();
    for (ET edge : getAdj()[v]) {
      if (edge.w() == w) {
        edges.add(edge);
      }
    }
    return edges;
  }

  @Override
  public void removeEdge(int v, int w) {
    validateVertex(v);
    validateVertex(w);
    getAdj()[v].removeIf(
        (o) -> {
          boolean rm = o.w() == w;
          if (rm) {
            --E;
          }
          return rm;
        });
  }

  @NotNull
  @Override
  public List<ET> removeNode(int v) {
    validateVertex(v);

    List<ET> relatedEdges = new ArrayList<>();

    Collection<ET> adjList = getAdj()[v];
    relatedEdges.addAll(adjList);
    adjList.clear();
    for (ET edge : edges()) {
      if (edge.w() == v) {
        relatedEdges.add(edge);
      }
    }
    removedNodes.add(v);
    return relatedEdges;
  }
}
