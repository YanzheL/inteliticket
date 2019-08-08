package org.yanzhe.inteliticket.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yanzhe.inteliticket.bean.AirlineInfoBean;
import org.yanzhe.inteliticket.bean.QueryParamBean;
import org.yanzhe.inteliticket.bean.QueryResultBean;
import org.yanzhe.inteliticket.core.algo.PathProvider;
import org.yanzhe.inteliticket.core.algo.YenKSP;
import org.yanzhe.inteliticket.core.graph.AirlineAdjacentListGraph;
import org.yanzhe.inteliticket.core.graph.DirectWeightedEdge;
import org.yanzhe.inteliticket.core.graph.GPath;
import org.yanzhe.inteliticket.core.graph.SimpleAdjacentListGraph;
import org.yanzhe.inteliticket.utils.GUILogger;

public class AirlineQuery {

  @NotNull
  private static GUILogger logger = new GUILogger();
  private AirlineAdjacentListGraph graph;
  private PathProvider<DirectWeightedEdge<AirlineInfoBean>> provider = new YenKSP<>(100);

  public static Set<String> queryMiddleSite(QueryParamBean param, int K) {
    AirlineAdjacentListGraph tpGraph = param.getGraph();
    PathProvider<DirectWeightedEdge<AirlineInfoBean>> provider = new YenKSP<>(K);

    List<GPath<DirectWeightedEdge<AirlineInfoBean>>> paths =
        provider.providePaths(
            new SimpleAdjacentListGraph<>(tpGraph),
            tpGraph.getCityCode(param.getSrc()),
            tpGraph.getCityCode(param.getDst()));
    Set<String> results = new TreeSet<>();
    for (GPath<DirectWeightedEdge<AirlineInfoBean>> path : paths) {
      for (int i : path.getAllTransSite()) {
        results.add(tpGraph.getCityName(i));
      }
    }

    return results;
  }

  @NotNull
  public static List<DirectWeightedEdge<AirlineInfoBean>> findAtDay(
      @NotNull List<DirectWeightedEdge<AirlineInfoBean>> edges, long date, boolean sort) {
    if (sort) {
      sortEdgesByStartTime(edges);
    }
    long nextDay = date + TimeUnit.DAYS.toMinutes(1);
    List<DirectWeightedEdge<AirlineInfoBean>> results = new ArrayList<>();
    for (DirectWeightedEdge<AirlineInfoBean> edge : edges) {
      AirlineInfoBean info = edge.getInfo();
      long start = info.getStartTime();
      if (start > date && start < nextDay) {
        results.add(edge);
      } else if (start >= nextDay) {
        break;
      }
    }
    return results;
  }

  public static void filterByToday(
      @NotNull List<DirectWeightedEdge<AirlineInfoBean>> edges, long date, boolean sort) {
    if (sort) {
      sortEdgesByStartTime(edges);
    }
    long nextDay = date + TimeUnit.DAYS.toMinutes(1);
    Iterator<DirectWeightedEdge<AirlineInfoBean>> iter = edges.iterator();
    while (iter.hasNext()) {
      DirectWeightedEdge<AirlineInfoBean> edge = iter.next();
      AirlineInfoBean info = edge.getInfo();
      long start = info.getStartTime();
      if (!(start > date && start < nextDay)) {
        iter.remove();
      }
    }
  }

  //  @Nullable
  //  private List<QueryResultBean> searchCustom(@NotNull QueryParamBean param) {
  //    List<GPath<DirectWeightedEdge<AirlineInfoBean>>> paths = new ArrayList<>();
  //
  //    SimpleAdjacentListGraph<DirectWeightedEdge<AirlineInfoBean>> simpleGraph =
  //        new SimpleAdjacentListGraph<>(graph);
  //
  //    if (param.getTransCities().size() > 0) {
  //      List<String> transCities = new ArrayList<>(param.getTransCities());
  //
  //      PathProvider<DirectWeightedEdge<AirlineInfoBean>> oneRouteProvider = new YenKSP<>(1);
  //      //      Queue<Integer[]> queue = new LinkedList<>();
  //
  //      GPath<DirectWeightedEdge<AirlineInfoBean>> path = new GPath<>();
  //
  //      Queue<Integer[]> queue = new LinkedList<>();
  //
  //      int i = 0, lastCode = graph.getCityCode(transCities.get(0));
  //      for (String city : transCities) {
  //
  //        int start, end;
  //        if (i == 0) {
  //          start = graph.getCityCode(param.getSrc());
  //          end = lastCode;
  //
  //        } else {
  //          start = lastCode;
  //          end = graph.getCityCode(city);
  //          lastCode = end;
  //        }
  //        List<GPath<DirectWeightedEdge<AirlineInfoBean>>> res =
  //            oneRouteProvider.providePaths(simpleGraph, start, end);
  //        if (res.size() > 0) {
  //          path.addPath(res.get(0));
  //        } else {
  //          logger.log("指定的中转航线不存在");
  //        }
  //        ++i;
  //      }
  //      path.addPath(
  //          oneRouteProvider
  //              .providePaths(simpleGraph, lastCode, graph.getCityCode(param.getDst()))
  //              .get(0));
  //      paths.add(path);
  //
  //    } else {
  //      paths =
  //          provider.providePaths(
  //              simpleGraph, graph.getCityCode(param.getSrc()),
  // graph.getCityCode(param.getDst()));
  //
  //      paths.removeIf((o) -> o.size() != param.getTransNum() + 1);
  //      //
  //      //      List<GPath<DirectWeightedEdge<AirlineInfoBean>>> paths = new ArrayList<>();
  //      //      for (GPath<DirectWeightedEdge<AirlineInfoBean>> path : rawPaths) {
  //      //        if (path.size() == param.getTransNum() + 1) paths.add(path);
  //      //      }
  //    }
  //
  //    return processPaths(paths, param);
  //  }

  public static DirectWeightedEdge<AirlineInfoBean> getNextValidEdge(
      @NotNull List<DirectWeightedEdge<AirlineInfoBean>> edges,
      long prevStart,
      long duration,
      long deadline,
      boolean sort) {
    if (sort) {
      sortEdgesByStartTime(edges);
    }
    for (DirectWeightedEdge<AirlineInfoBean> edge : edges) {
      AirlineInfoBean info = edge.getInfo();
      long curStart = info.getStartTime();
      if (curStart > prevStart
          && curStart - prevStart > duration
          && curStart + info.getDuration() < deadline) {
        return edge;
      }
    }
    return null;
  }

  public static void sortEdgesByStartTime(
      @NotNull List<DirectWeightedEdge<AirlineInfoBean>> edges) {
    edges.sort((o1, o2) -> Long.compare(o1.getInfo().getStartTime(), o2.getInfo().getStartTime()));
  }

  @Nullable
  public List<QueryResultBean> query(@NotNull QueryParamBean param) {
    graph = param.getGraph();
    if (param.isAuto()) {
      return searchAuto(param);
    } else if (param.getTransNum() == 0 && param.getTransCities().size() == 0) {
      return searchDirect(param);
    } else {
      return searchCustom(param);
    }
  }

  @Nullable
  private List<QueryResultBean> searchCustom(@NotNull QueryParamBean param) {
    List<GPath<DirectWeightedEdge<AirlineInfoBean>>> paths =
        provider.providePaths(
            new SimpleAdjacentListGraph<>(graph),
            graph.getCityCode(param.getSrc()),
            graph.getCityCode(param.getDst()));

    if (param.getTransCities().size() > 0) {
      List<Integer> requiredTransCitiesCode = new ArrayList<>();
      for (String cityName : param.getTransCities()) {
        requiredTransCitiesCode.add(graph.getCityCode(cityName));
      }
      paths.removeIf((o) -> !o.getAllTransSite().containsAll(requiredTransCitiesCode));
    } else {
      paths.removeIf((o) -> o.size() != param.getTransNum() + 1);
    }

    return processPaths(paths, param);
  }

  @Nullable
  private List<QueryResultBean> searchAuto(@NotNull QueryParamBean param) {
    List<GPath<DirectWeightedEdge<AirlineInfoBean>>> paths =
        provider.providePaths(
            new SimpleAdjacentListGraph<>(graph),
            graph.getCityCode(param.getSrc()),
            graph.getCityCode(param.getDst()));

    return processPaths(paths, param);
  }

  private List<QueryResultBean> processPaths(
      @NotNull List<GPath<DirectWeightedEdge<AirlineInfoBean>>> paths,
      @NotNull QueryParamBean param) {
    if (paths.size() == 0) {
      return null;
    }
    List<QueryResultBean> results = new ArrayList<>();
    long deadline =
        param.isTodayOnly() ? param.getDate() + TimeUnit.DAYS.toMinutes(1) : Long.MAX_VALUE;
    for (GPath<DirectWeightedEdge<AirlineInfoBean>> path : paths) {
      //      logger.log("----------");
      //      for (DirectWeightedEdge<AirlineInfoBean> edge : path) {
      //        logger.log(edge.toString());
      //      }
      //      logger.log("----------");
      List<List<DirectWeightedEdge<AirlineInfoBean>>> expanded = expandSinglePath(path);
      int routeLength = expanded.size();
      if (routeLength > 0) {
        filterByToday(expanded.get(0), param.getDate(), false);
        List<DirectWeightedEdge<AirlineInfoBean>> todayAirlines = expanded.get(0);
        if (routeLength == 1) {
          results.addAll(makeOneResult(todayAirlines));
        } else {
          List<List<DirectWeightedEdge<AirlineInfoBean>>> plans = new ArrayList<>();
          for (DirectWeightedEdge<AirlineInfoBean> todayAirline : todayAirlines) {
            List<DirectWeightedEdge<AirlineInfoBean>> plan = new ArrayList<>();
            boolean validPlan = true;
            plan.add(todayAirline);
            Queue<ImmutablePair<DirectWeightedEdge<AirlineInfoBean>, Integer>> midAirlines =
                new LinkedList<>();
            midAirlines.offer(new ImmutablePair<>(todayAirline, 1));
            while (!midAirlines.isEmpty()) {
              ImmutablePair<DirectWeightedEdge<AirlineInfoBean>, Integer> pair = midAirlines.poll();
              DirectWeightedEdge<AirlineInfoBean> site = pair.getKey();
              int index = pair.getValue();
              if (index >= routeLength) {
                break;
              }
              AirlineInfoBean siteInfo = site.getInfo();
              long endTime = siteInfo.getStartTime() + siteInfo.getDuration();
              DirectWeightedEdge<AirlineInfoBean> nextAirline =
                  getNextValidEdge(
                      expanded.get(index), endTime, param.getDuration(), deadline, false);
              if (nextAirline != null) {
                plan.add(nextAirline);
                midAirlines.offer(new ImmutablePair<>(nextAirline, index + 1));
              } else {
                validPlan = false;
                break;
              }
            }
            if (validPlan) {
              plans.add(plan);
            }
          }
          results.addAll(makeResults(plans));
        }
      }
    }
    return results;
  }

  @NotNull
  private List<QueryResultBean> makeResults(
      @NotNull List<List<DirectWeightedEdge<AirlineInfoBean>>> plans) {
    List<QueryResultBean> results = new ArrayList<>();
    for (List<DirectWeightedEdge<AirlineInfoBean>> plan : plans) {
      results.add(makeResult(plan));
    }
    return results;
  }

  @NotNull
  private List<QueryResultBean> makeOneResult(
      @NotNull List<DirectWeightedEdge<AirlineInfoBean>> plans) {
    List<QueryResultBean> results = new ArrayList<>();

    plans.stream().parallel().forEach((o) -> results.add(makeResult(o)));
    //    for (DirectWeightedEdge<AirlineInfoBean> plan : plans) {
    //      results.add(makeResult(plan));
    //    }
    return results;
  }

  private QueryResultBean makeResult(@NotNull DirectWeightedEdge<AirlineInfoBean> plan) {
    AirlineInfoBean info = plan.getInfo();

    long headStart = info.getStartTime();
    long tailEnd = info.getStartTime() + info.getDuration();

    return new QueryResultBean(
        0,
        graph.getCityName(plan.from()),
        graph.getCityName(plan.to()),
        headStart,
        tailEnd,
        tailEnd - headStart,
        info.getPrice(),
        Collections.singletonList(plan));
  }

  private QueryResultBean makeResult(@NotNull List<DirectWeightedEdge<AirlineInfoBean>> plan) {

    sortEdgesByStartTime(plan);
    DirectWeightedEdge<AirlineInfoBean> head = plan.get(0);
    DirectWeightedEdge<AirlineInfoBean> tail = plan.get(plan.size() - 1);

    long headStart = head.getInfo().getStartTime();
    AirlineInfoBean tailInfo = tail.getInfo();
    long tailEnd = tailInfo.getStartTime() + tailInfo.getDuration();

    return new QueryResultBean(
        plan.size() - 1,
        graph.getCityName(head.from()),
        graph.getCityName(tail.to()),
        headStart,
        tailEnd,
        tailEnd - headStart,
        sumPrice(plan),
        plan);
  }

  private double sumPrice(@NotNull List<DirectWeightedEdge<AirlineInfoBean>> plan) {
    double sum = 0;
    for (DirectWeightedEdge<AirlineInfoBean> edge : plan) {
      sum += edge.getInfo().getPrice();
    }
    return sum;
  }

  @NotNull
  public List<QueryResultBean> searchDirect(@NotNull QueryParamBean param) {
    int src = graph.getCityCode(param.getSrc());
    int dst = graph.getCityCode(param.getDst());
    List<DirectWeightedEdge<AirlineInfoBean>> edges = graph.getEdge(src, dst);
    List<QueryResultBean> results = new ArrayList<>();
    List<DirectWeightedEdge<AirlineInfoBean>> airlineAtDay =
        findAtDay(edges, param.getDate(), true);

    for (DirectWeightedEdge<AirlineInfoBean> edge : airlineAtDay) {
      AirlineInfoBean info = edge.getInfo();
      List<DirectWeightedEdge<AirlineInfoBean>> oneDetail = new ArrayList<>();
      oneDetail.add(edge);
      QueryResultBean res =
          new QueryResultBean(
              0,
              param.getSrc(),
              param.getDst(),
              info.getStartTime(),
              info.getStartTime() + info.getDuration(),
              info.getDuration(),
              info.getPrice(),
              oneDetail);
      results.add(res);
    }

    return results;
  }

  @NotNull
  public List<List<DirectWeightedEdge<AirlineInfoBean>>> expandSinglePath(
      @NotNull Iterable<DirectWeightedEdge<AirlineInfoBean>> linkedRoute) {
    List<List<DirectWeightedEdge<AirlineInfoBean>>> results = new ArrayList<>();
    for (DirectWeightedEdge<AirlineInfoBean> edge : linkedRoute) {
      List<DirectWeightedEdge<AirlineInfoBean>> multiedge = graph.getEdge(edge.from(), edge.to());
      sortEdgesByStartTime(multiedge);
      results.add(multiedge);
    }
    return results;
  }

  public void findAllTransRoutes(@NotNull List<DirectWeightedEdge<AirlineInfoBean>> linkedRoute) {
    List<List<DirectWeightedEdge<AirlineInfoBean>>> unfilteredRoutes =
        expandSinglePath(linkedRoute);
  }
}
