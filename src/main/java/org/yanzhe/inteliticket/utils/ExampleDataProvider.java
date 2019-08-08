//package org.yanzhe.inteliticket.utils;
//
//import org.yanzhe.inteliticket.bean.QueryResultBean;
//import org.yanzhe.inteliticket.gui.components.ResultRowUICmp;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class ExampleDataProvider {
//  public static List<QueryResultBean> provideRows(String src, String dst) {
//    List<QueryResultBean> rows = new ArrayList<>();
//    try {
//      rows.add(
//          new QueryResultBean(
//              0,
//              src,
//              dst,
//              TimeUnit.MILLISECONDS.toMinutes(UIUtils.hourMinFM.parse("21:10").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(UIUtils.hourMinFM.parse("23:30").getTime()),
//              140,
//              500,
//              null));
//      rows.add(
//          new QueryResultBean(
//              0,
//              src,
//              dst,
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("6:55").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("9:25").getTime()),
//              150,
//              610,
//              null));
//      rows.add(
//          new QueryResultBean(
//              0,
//              src,
//              dst,
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("10:15").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("12:40").getTime()),
//              135,
//              790,
//              null));
//      rows.add(
//          new QueryResultBean(
//              0,
//              src,
//              dst,
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("7:45").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("10:10").getTime()),
//              145,
//              840,
//              null));
//      rows.add(
//          new QueryResultBean(
//              0,
//              src,
//              dst,
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("7:00").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("9:20").getTime()),
//              140,
//              920,
//              null));
//      rows.add(
//          new QueryResultBean(
//              0,
//              src,
//              dst,
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("8:00").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(ResultRowUICmp.hourMinFM.parse("10:15").getTime()),
//              135,
//              1230,
//              null));
//
//    } catch (Exception e) {
//
//    }
//    return QueryResultBeanUtils.sort(rows, QueryResultBeanUtils.PRICE);
//  }
//}
