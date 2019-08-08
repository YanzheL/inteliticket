package org.yanzhe.inteliticket.utils;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.yanzhe.inteliticket.bean.QueryResultBean;

public class QueryResultBeanUtils {

  public static final int TRANSNUM = 0;
  public static final int DEPARTTIME = 1;
  public static final int ARRIVETIME = 2;
  public static final int DURATION = 3;
  public static final int PRICE = 4;

  public static List<QueryResultBean> sort(@NotNull List<QueryResultBean> beans, int field) {
    return sort(beans, field, true);
  }

  public static List<QueryResultBean> sort(@NotNull List<QueryResultBean> beans, int field,
      boolean asscending) {
    switch (field) {
      case TRANSNUM:
        beans.sort(Comparator.comparingInt(QueryResultBean::getTransNum));
        break;
      case DEPARTTIME:
        beans.sort(Comparator.comparingLong(QueryResultBean::getDepartTime));
        break;
      case ARRIVETIME:
        beans.sort(Comparator.comparingLong(QueryResultBean::getArriveTime));
        break;
      case DURATION:
        beans.sort(Comparator.comparingLong(QueryResultBean::getDuration));
        break;
      case PRICE:
        beans.sort(Comparator.comparingDouble(QueryResultBean::getPrice));
        break;
    }
    if (asscending) {
      return beans;
    } else {
      return Lists.reverse(beans);
    }
  }
}
