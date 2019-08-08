package org.yanzhe.inteliticket.core.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GPath<ET extends DirectWeightedEdge> implements Comparable<GPath<ET>>, Iterable<ET> {

  private List<ET> edges;
  private double weight;

  public GPath() {
    this.edges = new ArrayList<>();
  }

  public GPath(@NotNull List<ET> edges) {
    this();
    this.edges.addAll(edges);
    getWeight(true);
  }

  public GPath(@NotNull GPath<ET> path) {
    this(path.getEdges());
    getWeight(true);
  }

  @Override
  public int compareTo(@NotNull GPath<ET> o) {
    return Double.compare(weight, o.getWeight());
  }

  public boolean equals(@NotNull GPath<ET> obj) {
    if (edges.size() != obj.size()) {
      return false;
    } else {
      int i = 0;
      for (ET edge : edges) {
        if (!edge.equals(obj.getEdge(i))) {
          return false;
        }
        ++i;
      }
      return true;
    }
  }

  public void addEdge(@NotNull ET edge) {
    assert edges.size() == 0 || edges.get(edges.size() - 1).to() == edge.from()
        : "Cannot link path";
    edges.add(edge);
    weight += edge.getWeight();
  }

  @NotNull
  @Override
  public Iterator<ET> iterator() {
    return edges.iterator();
  }

  public int size() {
    return edges.size();
  }

  public double getWeight() {
    return getWeight(false);
  }

  public ET getEdge(int i) {
    return edges.get(i);
  }

  public List<ET> getEdges() {
    return edges;
  }

  @NotNull
  @SuppressWarnings("unchecked")
  public GPath<ET> cloneTo(int i) {
    LinkedList<ET> newEdges = new LinkedList<ET>();
    int l = this.edges.size();
    if (i > l) {
      i = l;
    }

    // for (Edge edge : this.edges.subList(0,i)) {
    for (int j = 0; j < i; j++) {
      // TODO Maybe clone()
      newEdges.add(edges.get(j));
    }

    return new GPath<>(newEdges);
  }

  public double getWeight(boolean calculate) {
    if (calculate) {
      weight = 0.0;
      for (ET edge : edges) {
        weight += edge.getWeight();
      }
    }

    return weight;
  }

  public void addPath(@NotNull GPath<ET> path) {
    edges.addAll(path.getEdges());
    weight += path.getWeight();
  }

  public List<Integer> getAllTransSite() {
    List<Integer> results = new ArrayList<>();
    for (ET edge : edges) {
      results.add(edge.to());
    }
    results.remove(results.size() - 1);
    return results;
  }
}
