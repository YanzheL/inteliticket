package org.yanzhe.inteliticket.core.graph;

import org.jetbrains.annotations.NotNull;

public class GeneralEdge<I> {

  protected int v;
  protected int w;
  protected I info;

  public GeneralEdge(int v, int w, I info) {
    this.v = v;
    this.w = w;
    this.info = info;
  }

  public GeneralEdge(@NotNull GeneralEdge<I> src) {
    this.v = src.v;
    this.w = src.w;
    this.info = src.info;
  }

  public I getInfo() {
    return info;
  }

  public void setInfo(I i) {
    info = i;
  }

  public int v() {
    return v;
  }

  public int w() {
    return w;
  }

//  public int getOne() {
//    return v;
//  }
//
//  public int getOther(int vertex) {
//    if (vertex == v) return w;
//    else if (vertex == w) return v;
//    else throw new RuntimeException("Inconsistent edge");
//  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof GeneralEdge) {
      GeneralEdge o = (GeneralEdge) obj;
      return v == o.v() && w == o.w() && info.equals(o.getInfo());
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return String.format("\nGeneralEdge [%d-%d] info = %s", v, w, info);
  }

  //  @Override
  //  protected GeneralEdge<I> clone() throws CloneNotSupportedException {
  //    return new GeneralEdge<>(this);
  //  }
}
