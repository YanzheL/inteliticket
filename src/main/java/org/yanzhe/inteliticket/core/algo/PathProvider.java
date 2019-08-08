package org.yanzhe.inteliticket.core.algo;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.yanzhe.inteliticket.core.graph.AbstractAdjacentListGraph;
import org.yanzhe.inteliticket.core.graph.DirectWeightedEdge;
import org.yanzhe.inteliticket.core.graph.GPath;

public interface PathProvider<ET extends DirectWeightedEdge> {

  @NotNull List<GPath<ET>> providePaths(AbstractAdjacentListGraph<ET> graph, int src, int dst);
}
