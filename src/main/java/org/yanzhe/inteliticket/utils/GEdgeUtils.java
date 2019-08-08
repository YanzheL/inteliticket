package org.yanzhe.inteliticket.utils;

import java.util.List;
import java.util.function.ToDoubleFunction;
import org.jetbrains.annotations.NotNull;
import org.yanzhe.inteliticket.bean.AirlineInfoBean;
import org.yanzhe.inteliticket.core.graph.DirectWeightedEdge;

public class GEdgeUtils {

  public static double sumWeight(@NotNull List<DirectWeightedEdge> edges) {
    //        double sum=0;
    //        for (DirectWeightedEdge edge:edges){
    //            sum+=edge.getWeight();
    //        }
    //        return sum;
    return sumBy(edges, DirectWeightedEdge::getWeight);
  }

  public static double sumPrice(@NotNull List<DirectWeightedEdge> edges) {
    return sumBy(
        edges,
        (o) -> {
          AirlineInfoBean info = (AirlineInfoBean) o.getInfo();
          return info.getPrice();
        });
  }

  public static double sumBy(@NotNull List<DirectWeightedEdge> edges,
      @NotNull ToDoubleFunction<DirectWeightedEdge> extractFunc) {
    double sum = 0;
    for (DirectWeightedEdge edge : edges) {
      sum += extractFunc.applyAsDouble(edge);
    }
    return sum;
  }
}
