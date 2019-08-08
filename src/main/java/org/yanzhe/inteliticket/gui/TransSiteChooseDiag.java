package org.yanzhe.inteliticket.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.yanzhe.inteliticket.bean.QueryParamBean;
import org.yanzhe.inteliticket.gui.components.TransOptionRowUICmp;

public class TransSiteChooseDiag extends JDialog {

  private JPanel rootPN;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JPanel actionPN;
  private JPanel btnPN;
  private JPanel mainPN;
  private JLabel titleLB;
  private JScrollPane mainSPN;
  private JPanel optionHolderPN;
  private QueryParamBean param;
  private JComponent flipArea;

  public TransSiteChooseDiag(QueryParamBean param, JComponent cmp) {
    this.flipArea = cmp;
    this.param = param;
    //    this.param = param;
    setContentPane(rootPN);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            onOK();
          }
        });

    buttonCancel.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            onCancel();
          }
        });

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            onCancel();
          }
        });

    // call onCancel() on ESCAPE
    rootPN.registerKeyboardAction(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            onCancel();
          }
        },
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOK() {
    // add your code here
    param.addTransCities(getSelectedCities());
//    flipArea.setEnabled(false);
//      UIUtils.enableComponents(flipArea,false);
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  //  public static void main(String[] args) {
  //    TransSiteChooseDiag dialog = new TransSiteChooseDiag();
  //    dialog.pack();
  //    dialog.setVisible(true);
  //    System.exit(0);
  //  }

  private void createUIComponents() {
    optionHolderPN = new JPanel();
    optionHolderPN.setLayout(new BoxLayout(optionHolderPN, BoxLayout.Y_AXIS));
  }

  public void updateSiteList(Iterable<String> sites) {
    optionHolderPN.removeAll();
    for (JComponent component : TransOptionRowUICmp.build(sites)) {
      optionHolderPN.add(component);
    }
    optionHolderPN.updateUI();
  }

  public Set<String> getSelectedCities() {
    Set<String> selectedCities = new TreeSet<>();
    for (Component rowPN : optionHolderPN.getComponents()) {
      JCheckBox box = (JCheckBox) ((JComponent) rowPN).getComponent(0);
      if (box.isSelected()) {
        selectedCities.add(box.getText());
      }
    }
    return selectedCities;
  }
}
