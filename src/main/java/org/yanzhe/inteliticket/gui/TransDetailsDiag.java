package org.yanzhe.inteliticket.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jetbrains.annotations.NotNull;
import org.yanzhe.inteliticket.bean.QueryResultBean;
import org.yanzhe.inteliticket.core.graph.AirlineAdjacentListGraph;
import org.yanzhe.inteliticket.gui.components.TransDetailRowUICmp;

public class TransDetailsDiag extends JDialog {

  private JPanel contentPane;
  private JButton buyBtn;
  private JLabel titleLB;
  private JScrollPane detailsSPN;
  private JPanel detailsPN;
  private JPanel headerPN;
  private JLabel srcLB;
  private JLabel dstLB;
  private JLabel durationLB;
  private JLabel priceLB;
  private JPanel okPN;
  private JPanel bundlePN;
  private JLabel companyLB;
  private JLabel seriesLB;
  private JButton cancelBtn;
  private AirlineAdjacentListGraph graph;

  public TransDetailsDiag(AirlineAdjacentListGraph graph) {
    this.graph = graph;
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buyBtn);

    buyBtn.addActionListener((evt) -> onOK());
    cancelBtn.addActionListener((evt) -> onCancel());
  }

  private void onOK() {
    // add your code here
    dispose();
  }

  private void onCancel() {
    dispose();
  }

  private void createUIComponents() {
    detailsPN = new JPanel();
    detailsPN.setLayout(new BoxLayout(detailsPN, BoxLayout.Y_AXIS));

    // TODO: place custom component creation code here
  }

  public void updateTransDetails(@NotNull QueryResultBean bean) {
    detailsPN.removeAll();
    for (JComponent component : TransDetailRowUICmp.build(bean.getDetails(), graph)) {
      detailsPN.add(component);
    }
    detailsPN.add(Box.createVerticalGlue());
    detailsPN.updateUI();
  }
}
