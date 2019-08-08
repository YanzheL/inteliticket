package org.yanzhe.inteliticket.gui.components;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yanzhe.inteliticket.bean.QueryResultBean;
import org.yanzhe.inteliticket.core.graph.AirlineAdjacentListGraph;
import org.yanzhe.inteliticket.gui.TransDetailsDiag;
import org.yanzhe.inteliticket.utils.UIUtils;

public class ResultRowUICmp {

  private JButton detailsBtn;
  private JLabel priceLB;
  private JPanel dstPN;
  private JLabel arriveTimeLB;
  private JLabel arriveCityLB;
  private JLabel arrowLB;
  private JPanel srcPN;
  private JLabel departTimeLB;
  private JLabel departCityLB;
  private JPanel rootPN;
  private JPanel rowPN;
  private JLabel transNumLB;
  private JLabel durationLB;
  private JPanel durationPN;
  @Nullable
  private QueryResultBean resultData;
  private AirlineAdjacentListGraph graph;

  public ResultRowUICmp(AirlineAdjacentListGraph graph) {
    this.graph = graph;
    detailsBtn.addActionListener(
        (e) -> {
          TransDetailsDiag dialog = new TransDetailsDiag(graph);
          dialog.updateTransDetails(resultData);
          dialog.pack();
          dialog.setVisible(true);
        });
  }

  @NotNull
  public static List<JComponent> build(@Nullable List<QueryResultBean> data,
      AirlineAdjacentListGraph graph) {
    List<JComponent> components = new ArrayList<>();
    if (data != null) {
      for (QueryResultBean bean : data) {
        ResultRowUICmp ui = new ResultRowUICmp(graph);
        components.add(ui.build(bean));
      }
    }
    return components;
  }

  @Nullable
  public JComponent build(@Nullable QueryResultBean res) {
    if (res == null) {
      return null;
    }
    resultData = res;
    transNumLB.setText(String.valueOf(res.getTransNum()));
    departTimeLB.setText(UIUtils.formatTimeFromLongMinutes(res.getDepartTime(), UIUtils.hourMinFM));
    departCityLB.setText(res.getSrc());
    durationLB.setText(String.format("%d分钟", res.getDuration()));
    arriveTimeLB.setText(UIUtils.formatTimeFromLongMinutes(res.getArriveTime(), UIUtils.hourMinFM));
    arriveCityLB.setText(res.getDst());
    priceLB.setText(String.format("￥%d", (int) res.getPrice()));
    return rowPN;
  }
}
