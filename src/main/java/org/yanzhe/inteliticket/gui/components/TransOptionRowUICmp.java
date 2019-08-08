package org.yanzhe.inteliticket.gui.components;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransOptionRowUICmp {

  private JPanel rootPN;
  private JPanel rowPN;
  private JCheckBox optCBBX;

  @NotNull
  public static List<JComponent> build(@NotNull Iterable<String> data) {
    List<JComponent> components = new ArrayList<>();
    for (String name : data) {
      TransOptionRowUICmp ui = new TransOptionRowUICmp();
      components.add(ui.build(name));
    }
    return components;
  }

  @Nullable
  public JComponent build(String cityName) {
    optCBBX.setText(cityName);
    return rowPN;
  }

  @Nullable
  public String isSelected() {
    if (optCBBX.isSelected()) {
      return optCBBX.getText();
    } else {
      return null;
    }
  }
}
