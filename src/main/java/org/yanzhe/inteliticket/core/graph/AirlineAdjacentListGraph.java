package org.yanzhe.inteliticket.core.graph;

import java.io.FileReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yanzhe.inteliticket.bean.AirlineInfoBean;
import org.yanzhe.inteliticket.utils.GUILogger;

public class AirlineAdjacentListGraph
    extends DefaultAdjacentListGraph<DirectWeightedEdge<AirlineInfoBean>> {

  protected static final DateFormat dayLevelDateFmt = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat srcDateFmt = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
  @NotNull
  private ArrayList<String> cityCodesTable = new ArrayList<>();
  private Trie<String, Integer> trie;

  public AirlineAdjacentListGraph() {
    super();
  }

  public static void main(String[] args) {
    AirlineAdjacentListGraph ag = new AirlineAdjacentListGraph();
    ag.init("/Users/liyanzhe/IdeaProjects/inteliticket/airlinedata_large.csv");
    ArrayList<DirectWeightedEdge<AirlineInfoBean>> ret =
        ag.search("北京", "上海", new Date(2018 - 1900, 0, 26));
    ret.sort(Comparator.naturalOrder());
    System.out.println(ret);
  }

  public boolean init(@NotNull String path) {
    trie = new PatriciaTrie<>();
    TreeMap<Integer, ArrayList<DirectWeightedEdge<AirlineInfoBean>>> tempAdj = new TreeMap<>();
    try (Reader in = new FileReader(path)) {
      Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
      long t1 = System.currentTimeMillis();
      SortedSet<Integer> seenCode = new TreeSet<>();
      for (CSVRecord record : records) {
        ++E;
        String srcName = record.get("src"), dstName = record.get("dst");
        int srcCode = getCityCode(srcName, true), dstCode = getCityCode(dstName, true);
        seenCode.add(srcCode);
        seenCode.add(dstCode);
        trie.put(srcName, srcCode);
        trie.put(dstName, dstCode);
        try {
          DirectWeightedEdge<AirlineInfoBean> edge =
              new DirectWeightedEdge<AirlineInfoBean>(
                  srcCode,
                  dstCode,
                  Double.valueOf(record.get("distance")),
                  new AirlineInfoBean(
                      Double.valueOf(record.get("price")),
                      TimeUnit.MILLISECONDS.toMinutes(
                          srcDateFmt.parse(record.get("startTime")).getTime()),
                      TimeUnit.MINUTES.toMinutes(Long.valueOf(record.get("duration"))),
                      record.get("company")));
          if (tempAdj.containsKey(srcCode)) {
            tempAdj.get(srcCode).add(edge);
          } else {
            ArrayList<DirectWeightedEdge<AirlineInfoBean>> nLink = new ArrayList<>();
            nLink.add(edge);
            tempAdj.put(srcCode, nLink);
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      new GUILogger()
          .log(String.format("加载时间 = %fms", ((double) (System.currentTimeMillis() - t1) / 1000)));
      initStorage(seenCode.last() + 1);
      Set<Map.Entry<Integer, ArrayList<DirectWeightedEdge<AirlineInfoBean>>>> sorted =
          tempAdj.entrySet();
      for (Map.Entry<Integer, ArrayList<DirectWeightedEdge<AirlineInfoBean>>> entry : sorted) {
        getAdj()[entry.getKey()] = entry.getValue();
      }
      initialized = true;
    } catch (Exception e) {
      e.printStackTrace();
      initialized = false;
    }
    return initialized;
  }

  @Override
  public void clear() {
    super.clear();
    cityCodesTable.clear();
  }

  public int getCityCode(String cityName) {
    return getCityCode(cityName, false);
  }

  private int getCityCode(String cityName, boolean insert) {
    int code = cityCodesTable.indexOf(cityName);
    if (insert) {
      if (code == -1) {
        cityCodesTable.add(cityName);
        return cityCodesTable.size() - 1;
      } else {
        return code;
      }
    } else {
      return code;
    }
  }

  @NotNull
  public ArrayList<DirectWeightedEdge<AirlineInfoBean>> search(String src, String dst) {
    return search(src, dst, -1);
  }

  @NotNull
  public ArrayList<DirectWeightedEdge<AirlineInfoBean>> search(
      String src, String dst, @Nullable Date start) {
    return search(src, dst, start == null ? -1 : TimeUnit.MILLISECONDS.toMinutes(start.getTime()));
  }

  @NotNull
  public ArrayList<DirectWeightedEdge<AirlineInfoBean>> search(String src, String dst, long start) {
    int srcCode = getCityCode(src, true), dstCode = getCityCode(dst, true);
    validateVertex(srcCode);
    Collection<DirectWeightedEdge<AirlineInfoBean>> list = getAdj()[srcCode];
    ArrayList<DirectWeightedEdge<AirlineInfoBean>> result = new ArrayList<>();
    for (DirectWeightedEdge<AirlineInfoBean> e : list) {
      int w = e.to();
      if (w == dstCode) {
        AirlineInfoBean info = e.info;
        if (start == -1 || start == info.getStartTime()) {
          result.add(e);
        }
      }
    }
    return result;
  }

//  protected void validateVertex(int v) {
//    if (v < 0 || v >= V)
//      throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
//  }

  @NotNull
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder(String.format("总城市数 = %d, 总航班数 = %d\n", V, E));
    return s.toString();
  }

  public String getCityName(int cityCode) {
    validateVertex(cityCode);
    return cityCodesTable.get(cityCode);
  }

  @NotNull
  public Set<Map.Entry<String, Integer>> searchCity(String key) {
    //    Set<Map.Entry<String, Integer>> result=new
    //    int curIndex=key.length()-1;
    //    boolean ctn=true;
    //    while (ctn){
    //      Set found=trie.prefixMap(key.substring(0,curIndex)).entrySet();
    //      if (found.size()>0)
    //    }

    return trie.prefixMap(key).entrySet();
  }
}
