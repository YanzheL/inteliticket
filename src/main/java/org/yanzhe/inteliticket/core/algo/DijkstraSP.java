package org.yanzhe.inteliticket.core.algo;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yanzhe.inteliticket.core.IndexMinPQ;
import org.yanzhe.inteliticket.core.graph.AbstractAdjacentListGraph;
import org.yanzhe.inteliticket.core.graph.DirectWeightedEdge;
import org.yanzhe.inteliticket.core.graph.GPath;

public class DijkstraSP<ET extends DirectWeightedEdge> implements PathProvider<ET> {

  private double[] distTo; // distTo[v] = distance  of shortest s->v path
  private ET[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
  private IndexMinPQ<Double> pq; // priority queue of vertices
  private AbstractAdjacentListGraph<ET> G;
  private boolean initialized = false;

  //  public DijkstraSP(DefaultAdjacentListGraph<ET> G) {
  //    init(G);
  //  }
  //
  //  public DijkstraSP(SimpleAdjacentListGraph<ET> G) {
  //    init(G);
  //  }

  public DijkstraSP() {
  }

  //  public void init(DefaultAdjacentListGraph<ET> G) {
  //    init(new SimpleAdjacentListGraph<>(G));
  //  }

  public void init(@NotNull AbstractAdjacentListGraph<ET> G) {
    if (!initialized) {
      this.G = G;
      for (DirectWeightedEdge e : G.edges()) {
        if (e.getWeight() < 0) {
          throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
      }
      distTo = new double[G.V()];
      edgeTo = (ET[]) (new DirectWeightedEdge[G.V()]);

      pq = new IndexMinPQ<>(G.V());
      initialized = true;
    }
  }

  public void run(int s) {
    clear();
    validateVertex(s);
    distTo[s] = 0.0;
    pq.insert(s, distTo[s]);
    while (!pq.isEmpty()) {
      int v = pq.delMin();
      for (ET e : G.adj(v)) {
        relax(e);
      }
    }
    // check optimality conditions
    assert check(G, s);
  }

  private void clear() {
    for (int v = 0; v < G.V(); v++) {
      distTo[v] = Double.POSITIVE_INFINITY;
    }
    for (int i = 0; i < edgeTo.length; ++i) {
      edgeTo[i] = null;
    }
    pq.clear();
    initialized = false;
  }

  // relax edge e and update pq if changed
  private void relax(@NotNull ET e) {
    int v = e.from(), w = e.to();
    if (distTo[w] > distTo[v] + e.getWeight()) {
      distTo[w] = distTo[v] + e.getWeight();
      edgeTo[w] = e;
      if (pq.contains(w)) {
        pq.decreaseKey(w, distTo[w]);
      } else {
        pq.insert(w, distTo[w]);
      }
    }
  }

  /**
   * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
   *
   * @param v the destination vertex
   * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
   * {@code Double.POSITIVE_INFINITY} if no such path
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  public double distTo(int v) {
    validateVertex(v);
    return distTo[v];
  }

  /**
   * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
   *
   * @param v the destination vertex
   * @return {@code true} if there is a path from the source vertex {@code s} to vertex {@code v};
   * {@code false} otherwise
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  public boolean hasPathTo(int v) {
    validateVertex(v);
    return distTo[v] < Double.POSITIVE_INFINITY;
  }

  /**
   * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
   *
   * @param v the destination vertex
   * @return a shortest path from the source vertex {@code s} to vertex {@code v} as an iterable of
   * edges, and {@code null} if no such path
   * @throws IllegalArgumentException unless {@code 0 <= v < V}
   */
  @Nullable
  public Iterable<ET> pathTo(int v) {
    validateVertex(v);
    if (!hasPathTo(v)) {
      return null;
    }
    List<ET> path = new ArrayList<>();
    for (ET e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
      path.add(e);
    }
    return Lists.reverse(path);
  }

  @NotNull
  @Override
  public List<GPath<ET>> providePaths(@NotNull AbstractAdjacentListGraph<ET> graph, int src,
      int dst) {
    init(graph);
    run(src);
    GPath<ET> path = new GPath<>();
    Iterable<ET> itr = pathTo(dst);

    if (itr != null) {
      for (ET edge : pathTo(dst)) {
        path.addEdge(edge);
      }
      return Collections.singletonList(path);
    } else {
      return Collections.emptyList();
    }
  }

  // check optimality conditions:
  // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
  // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
  private boolean check(@NotNull AbstractAdjacentListGraph<ET> G, int s) {

    // check that edge weights are nonnegative
    for (DirectWeightedEdge e : G.edges()) {
      if (e.getWeight() < 0) {
        System.err.println("negative edge weight detected");
        return false;
      }
    }

    // check that distTo[v] and edgeTo[v] are consistent
    if (distTo[s] != 0.0 || edgeTo[s] != null) {
      System.err.println("distTo[s] and edgeTo[s] inconsistent");
      return false;
    }
    for (int v = 0; v < G.V(); v++) {
      if (v == s) {
        continue;
      }
      if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
        System.err.println("distTo[] and edgeTo[] inconsistent");
        return false;
      }
    }

    // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
    for (int v = 0; v < G.V(); v++) {
      for (DirectWeightedEdge e : G.adj(v)) {
        int w = e.to();
        if (distTo[v] + e.getWeight() < distTo[w]) {
          System.err.println("edge " + e + " not relaxed");
          return false;
        }
      }
    }

    // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
    for (int w = 0; w < G.V(); w++) {
      if (edgeTo[w] == null) {
        continue;
      }
      DirectWeightedEdge e = edgeTo[w];
      int v = e.from();
      if (w != e.to()) {
        return false;
      }
      if (distTo[v] + e.getWeight() != distTo[w]) {
        System.err.println("edge " + e + " on shortest path not tight");
        return false;
      }
    }
    return true;
  }

  // throw an IllegalArgumentException unless {@code 0 <= v < V}
  private void validateVertex(int v) {
    int V = distTo.length;
    if (v < 0 || v >= V) {
      throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
  }
}
