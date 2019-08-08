package org.yanzhe.inteliticket.gui.components;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yanzhe.inteliticket.bean.AirlineInfoBean;
import org.yanzhe.inteliticket.core.graph.AirlineAdjacentListGraph;
import org.yanzhe.inteliticket.core.graph.DirectWeightedEdge;
import org.yanzhe.inteliticket.utils.UIUtils;

public class TransDetailRowUICmp {

  private JPanel rootPN;
  private JPanel rowPN;
  private JLabel priceLB;
  private JPanel dstPN;
  private JLabel arriveTimeLB;
  private JLabel arriveCityLB;
  private JPanel durationPN;
  private JPanel srcPN;
  private JLabel durationLB;
  private JLabel arrowLB;
  private JLabel departTimeLB;
  private JLabel departCityLB;
  private JPanel markPN;
  private JLabel seriesLB;
  private JLabel companyLB;
  private AirlineInfoBean resultData;
  private AirlineAdjacentListGraph graph;

  public TransDetailRowUICmp(AirlineAdjacentListGraph graph) {
    this.graph = graph;
  }

  @NotNull
  public static List<JComponent> build(
      @NotNull List<DirectWeightedEdge<AirlineInfoBean>> data, AirlineAdjacentListGraph graph) {
    List<JComponent> components = new ArrayList<>();
    int i = 0;
    for (DirectWeightedEdge<AirlineInfoBean> bean : data) {
      TransDetailRowUICmp ui = new TransDetailRowUICmp(graph);
      components.add(ui.build(bean, i));
      ++i;
    }
    return components;
  }

  @Nullable
  public JComponent build(@Nullable DirectWeightedEdge<AirlineInfoBean> edge, int seriesNo) {
    if (edge == null) {
      return null;
    }
    AirlineInfoBean res = edge.getInfo();
    resultData = res;
    seriesLB.setText(String.valueOf(seriesNo));
    companyLB.setText(res.getCompany());
    departTimeLB.setText(UIUtils.formatTimeFromLongMinutes(res.getStartTime(), UIUtils.fullTimeFM));
    departCityLB.setText(graph.getCityName(edge.from()));
    durationLB.setText(String.format("%d分钟", res.getDuration()));
    arriveTimeLB.setText(
        UIUtils.formatTimeFromLongMinutes(
            res.getStartTime() + res.getDuration(), UIUtils.fullTimeFM));
    arriveCityLB.setText(graph.getCityName(edge.to()));
    priceLB.setText(String.format("￥%d", (int) res.getPrice()));
    return rowPN;
  }
}
