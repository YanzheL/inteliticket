package org.yanzhe.inteliticket.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.yanzhe.inteliticket.core.graph.AirlineAdjacentListGraph;

public class QueryParamBean {

  public static final int NO_ERROR = 0;
  public static final int EMPTY_STRING_ERROR = 1;
  public static final int TOO_SHORT_DURATION_ERROR = 2;
  public static final int NO_SUCH_CITY_ERROR = 3;
  public static final int SAME_CITY_ERROR = 4;
  @NotNull
  private HashMap<Integer, String> errorcodes = new HashMap<>();

  //  public static final int BY_NONE = -1;
  private AirlineAdjacentListGraph graph;
  private String src;
  private String dst;

  private SortedSet<String> transCities = new TreeSet<>();

  private long date;
  private boolean auto;
  private int transNum;

  private long duration = TimeUnit.HOURS.toMinutes(1);

  private boolean todayOnly;

  public void addTransCities(Collection<String> trans) {
    transCities.addAll(trans);
  }

  public SortedSet<String> getTransCities() {
    return transCities;
  }

  public AirlineAdjacentListGraph getGraph() {
    return graph;
  }

  public void setGraph(AirlineAdjacentListGraph graph) {
    this.graph = graph;
  }

  public boolean isTodayOnly() {
    return todayOnly;
  }

  public void setTodayOnly(boolean todayOnly) {
    this.todayOnly = todayOnly;
  }

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  public String getDst() {
    return dst;
  }

  public void setDst(String dst) {
    this.dst = dst;
  }

  public boolean isAuto() {
    return auto;
  }

  public void setAuto(boolean auto) {
    this.auto = auto;
  }

  public int getTransNum() {
    return transNum;
  }

  public void setTransNum(int transNum) {
    this.transNum = transNum;
  }

  public boolean check(@NotNull AirlineAdjacentListGraph graph) {
    //    System.out.println(src);
    //    System.out.println(dst);
    errorcodes.clear();
    if (src == null || src.isEmpty() || dst == null || dst.isEmpty()) {
      errorcodes.put(EMPTY_STRING_ERROR, "城市名不能为空");
    } else if (src.equals(dst)) {
      errorcodes.put(SAME_CITY_ERROR, String.format("始末地点不能相同", src));
    } else if (graph.getCityCode(src) == -1) {
      errorcodes.put(NO_SUCH_CITY_ERROR, String.format("图中不存在城市<%s>", src));
    } else if (graph.getCityCode(dst) == -1) {
      errorcodes.put(NO_SUCH_CITY_ERROR, String.format("图中不存在城市<%s>", dst));
    }
    if (!auto && transNum != 0) {
      if (duration < 60) {
        errorcodes.put(TOO_SHORT_DURATION_ERROR, "换乘时间至少应为1小时");
      }
    }
    return errorcodes.isEmpty();
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  @NotNull
  public HashMap<Integer, String> getErrorcodes() {
    return errorcodes;
  }
}
