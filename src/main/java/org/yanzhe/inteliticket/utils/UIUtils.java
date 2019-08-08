package org.yanzhe.inteliticket.utils;

import java.awt.Component;
import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class UIUtils {

  public static final SimpleDateFormat hourMinFM = new SimpleDateFormat("HH:mm");
  public static final SimpleDateFormat fullTimeFM = new SimpleDateFormat("MM月dd日 HH:mm");

  public static void enableComponents(@NotNull Container container, boolean enable) {
    Component[] components = container.getComponents();
    for (Component component : components) {
      component.setEnabled(enable);
      if (component instanceof Container) {
        enableComponents((Container) component, enable);
      }
    }
  }

  public static String formatTimeFromLongMinutes(long mins, SimpleDateFormat fmt) {
    return fmt.format(new Date(TimeUnit.MINUTES.toMillis(mins)));
  }

  //    DisableAllInContainer() {
  //        JPanel gui = new JPanel(new BorderLayout());
  //
  //        final JPanel container = new JPanel(new BorderLayout());
  //        gui.add(container, BorderLayout.CENTER);
  //
  //        JToolBar tb = new JToolBar();
  //        container.add(tb, BorderLayout.NORTH);
  //        for (int ii=0; ii<3; ii++) {
  //            tb.add(new JButton("Button"));
  //        }
  //
  //        JTree tree = new JTree();
  //        tree.setVisibleRowCount(6);
  //        container.add(new JScrollPane(tree), BorderLayout.WEST);
  //
  //        container.add(new JTextArea(5,20), BorderLayout.CENTER);
  //
  //        final JCheckBox enable = new JCheckBox("Enable", true);
  //        enable.addActionListener((ae)->enableComponents(container, enable.isSelected()));
  //        gui.add(enable, BorderLayout.SOUTH);
  //
  //        JOptionPane.showMessageDialog(null, gui);
  //    }

  //    public static void main(String[] args)  {
  //        SwingUtilities.invokeLater(DisableAllInContainer::new);
  //    }
}
