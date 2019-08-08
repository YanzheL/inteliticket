package org.yanzhe.inteliticket.core.graph;

import org.jetbrains.annotations.NotNull;

public class DirectWeightedEdge<I> extends GeneralEdge<I>
    implements Comparable<DirectWeightedEdge<I>> {

  protected double weight;

  public DirectWeightedEdge(int v, int w, double weight) {
    this(v, w, weight, null);
  }

  public DirectWeightedEdge(int v, int w, double weight, I info) {
    super(v, w, info);
    this.weight = weight;
  }

  public DirectWeightedEdge(@NotNull DirectWeightedEdge<I> src) {
    super(src);
    this.weight = src.getWeight();
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double w) {
    weight = w;
  }

  public int from() {
    return v;
  }

  public int to() {
    return w;
  }

  public int compareTo(DirectWeightedEdge<I> e) {

    return Double.compare(weight, e.getWeight());
  }

  @Override
  public String toString() {
    return String.format("\nEdge [%d-->%d] w = %.2f info = %s", v, w, weight, info);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DirectWeightedEdge) {
      DirectWeightedEdge o = (DirectWeightedEdge) obj;
      return super.equals(o) && weight == o.getWeight();
    } else {
      return false;
    }
  }

  @NotNull
  public DirectWeightedEdge<I> reverse() {
    return reverse(true);
  }

  @NotNull
  public DirectWeightedEdge<I> reverse(boolean modify) {
    if (modify) {
      int tp;
      tp = v;
      v = w;
      w = tp;
      return this;
    } else {
      DirectWeightedEdge<I> e = new DirectWeightedEdge<>(this);
      return e.reverse(true);
    }
  }
}
