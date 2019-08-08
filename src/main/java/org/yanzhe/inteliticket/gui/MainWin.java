package org.yanzhe.inteliticket.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yanzhe.inteliticket.bean.QueryParamBean;
import org.yanzhe.inteliticket.bean.QueryResultBean;
import org.yanzhe.inteliticket.core.AirlineQuery;
import org.yanzhe.inteliticket.core.graph.AirlineAdjacentListGraph;
import org.yanzhe.inteliticket.gui.components.ResultRowUICmp;
import org.yanzhe.inteliticket.utils.ExampleFileFilter;
import org.yanzhe.inteliticket.utils.GUILogger;
import org.yanzhe.inteliticket.utils.QueryResultBeanUtils;
import org.yanzhe.inteliticket.utils.UIUtils;

public class MainWin {

  public JFrame mainFrame;
  private JLabel loadDataLB;
  private JTextField dataPathTF;
  private JButton dataPathSelectBtn;
  private JLabel srcCityLB;
  private JComboBox<String> srcCityCBBX;
  private JTextComponent srcCityTextCp;
  private JLabel dstCityLB;
  private JComboBox<String> dstCityCBBX;
  private JTextComponent dstCityTextCp;
  private JLabel travelDateLB;
  private JSpinner dateSP;
  private JPanel rootPN;
  private JPanel mainPN;
  private JPanel infoInputPN;
  private JPanel dataLoadingPN;
  private JCheckBox autoSuggestCBX;
  private JButton queryBtn;
  private JRadioButton byPriceRB;
  private JRadioButton byTimeRB;
  private JLabel titleLB;
  private JTextArea logAreaTA;
  private JLabel resultTitleLB;
  private JButton loadDataBtn;
  private JScrollPane logSPN;
  private JLabel transNumLB;
  private JLabel stayTimeLB;
  private JLabel logAreaLB;
  private JScrollPane resultSPN;
  private JPanel resultAreaPN;
  private JPanel stayTimePN;
  private JComboBox<Integer> stayDaysCBBX;
  private JComboBox<Integer> stayHoursCBBX;
  private JComboBox<Integer> stayMinsCBBX;
  private JLabel stayDaysLB;
  private JLabel stayHoursLB;
  private JLabel stayMinsLB;
  private JComboBox<Integer> transNumCBBX;
  private JPanel resultHeaderPN;
  private JButton noneHeaderBtn;
  private JButton priceHeaderBtn;
  private JButton dstHeaderBtn;
  private JButton durationHeaderBtn;
  private JButton transNumHeaderBtn;
  private JButton srcHeaderBtn;
  private JPanel logPN;
  private JPanel resultPN;
  private JButton chooseTransBtn;
  private JPanel queryPN;
  private JPanel sidePN;
  private JCheckBox todayOnlyCBBX;
  private JPanel sideVtPN;
  private JButton flipBtn;
  @NotNull
  private AirlineAdjacentListGraph graph = new AirlineAdjacentListGraph();
  @Nullable
  private List<QueryResultBean> resultData;
  private boolean sortFlag1;
  private boolean sortFlag2;
  private boolean sortFlag3;
  private boolean sortFlag4;
  private boolean sortFlag5;
  private GUILogger logger;
  private InternalDoubleFlag dropDownUpdateFlag;
  private QueryParamBean pendingQuery;

  public MainWin() {
    logger = new GUILogger(this::log);

    dataPathSelectBtn.addActionListener(
        (evt) -> {
          JFileChooser fileChooser = new JFileChooser(); // 对话框
          fileChooser.setCurrentDirectory(new File("."));
          fileChooser.setAcceptAllFileFilterUsed(true);

          ExampleFileFilter filter = new ExampleFileFilter();
          filter.addExtension("csv");
          fileChooser.setFileFilter(filter);

          int returnVal = fileChooser.showOpenDialog(mainPN);
          if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile(); // 取得选中的文件
            dataPathTF.setText(selectedFile.getAbsolutePath()); // 取得路径
          }
        });
    loadDataBtn.addActionListener(
        (evt) -> {
          String path = dataPathTF.getText();
          backgroundRun(
              () -> {
                logger.log("正在构建图...");
                graph.clear();
                graph.init(path);
                logger.log("图构建成功");
                UIUtils.enableComponents(queryPN, true);
                logger.log(graph.toString());
              });
        });
    queryBtn.addActionListener(
        (evt) -> {
          QueryParamBean params;
          if (pendingQuery == null) {
            params = grabUserInputParams();
          } else {
            params = pendingQuery;
            pendingQuery = null;
          }

          if (!params.check(graph)) {
            ErrorInputMsgDiag errDiag = new ErrorInputMsgDiag();
            errDiag.setMsg(new Vector<>(params.getErrorcodes().values()));
            errDiag.pack();
            errDiag.setModal(true);
            errDiag.setLocationRelativeTo(mainFrame);
            errDiag.setVisible(true);
            return;
          }

          resultAreaPN.removeAll();
          backgroundRun(
              () -> {
                logger.log("正在后台查询...");
                AirlineQuery query = new AirlineQuery();
                resultData = query.query(params);
                // resultData =
                // ExampleDataProvider.provideRows(srcCityCBBX.getText(),dstCityCBBX.getText());
                updateDateList(resultData);
                logger.log("查询成功");
              });
        });
    autoSuggestCBX.addActionListener(
        (evt) -> {
          boolean selected = ((JCheckBox) (evt.getSource())).isSelected();
          chooseTransBtn.setEnabled(!selected);
          transNumLB.setEnabled(!selected);
          transNumCBBX.setEnabled(!selected);
          stayTimeLB.setEnabled(!selected);
          UIUtils.enableComponents(stayTimePN, !selected);
        });
    transNumHeaderBtn.addActionListener(
        (evt) -> {
          sortFlag1 = !sortFlag1;
          updateDateList(
              QueryResultBeanUtils.sort(resultData, QueryResultBeanUtils.TRANSNUM, sortFlag1));
        });

    srcHeaderBtn.addActionListener(
        (evt) -> {
          sortFlag2 = !sortFlag2;
          updateDateList(
              QueryResultBeanUtils.sort(resultData, QueryResultBeanUtils.DEPARTTIME, sortFlag2));
        });
    dstHeaderBtn.addActionListener(
        (evt) -> {
          sortFlag3 = !sortFlag3;
          updateDateList(
              QueryResultBeanUtils.sort(resultData, QueryResultBeanUtils.ARRIVETIME, sortFlag3));
        });
    durationHeaderBtn.addActionListener(
        (evt) -> {
          sortFlag4 = !sortFlag4;
          updateDateList(
              QueryResultBeanUtils.sort(resultData, QueryResultBeanUtils.DURATION, sortFlag4));
        });
    priceHeaderBtn.addActionListener(
        (evt) -> {
          sortFlag5 = !sortFlag5;
          updateDateList(
              QueryResultBeanUtils.sort(resultData, QueryResultBeanUtils.PRICE, sortFlag5));
        });
    dropDownUpdateFlag = new InternalDoubleFlag();
    srcCityTextCp = (JTextComponent) srcCityCBBX.getEditor().getEditorComponent();
    dstCityTextCp = (JTextComponent) dstCityCBBX.getEditor().getEditorComponent();
    srcCityTextCp
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(@NotNull DocumentEvent e) {
                SwingUtilities.invokeLater(() -> onInputChange(e, srcCityCBBX, true));
              }

              @Override
              public void removeUpdate(@NotNull DocumentEvent e) {
                //                  srcDropDownUpdated=false;
                //                lock2 = true;
                //                SwingUtilities.invokeLater(() -> onInputChange(e, srcCityCBBX));
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
              }
            });

    srcCityTextCp.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            if (srcCityTextCp.getText().length() > 0) {
              srcCityCBBX.showPopup();
            }
          }
        });
    dstCityTextCp
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(@NotNull DocumentEvent e) {
                SwingUtilities.invokeLater(() -> onInputChange(e, dstCityCBBX, false));
              }

              @Override
              public void removeUpdate(@NotNull DocumentEvent e) {
                //                                  SwingUtilities.invokeLater(()->onInputChange(e,
                // dstCityCBBX));
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
              }
            });
    dstCityTextCp.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            if (dstCityTextCp.getText().length() > 0) {
              dstCityCBBX.showPopup();
            }
          }
        });
    chooseTransBtn.addActionListener(
        (evt) -> {
          Set<String> sites = getRecommandTransCities();
          TransSiteChooseDiag chooseUi = new TransSiteChooseDiag(pendingQuery, transNumCBBX);
          chooseUi.updateSiteList(sites);
          chooseUi.setLocationRelativeTo(mainFrame);
          //            chooseUi.setPreferredSize(new Dimension(100,300));
          chooseUi.pack();
          chooseUi.setVisible(true);
        });
    flipBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String t1 = ((JTextComponent) srcCityCBBX.getEditor().getEditorComponent()).getText();
        String t2 = ((JTextComponent) dstCityCBBX.getEditor().getEditorComponent()).getText();
        ((JTextComponent) srcCityCBBX.getEditor().getEditorComponent()).setText(t2);
        ((JTextComponent) dstCityCBBX.getEditor().getEditorComponent()).setText(t1);
      }
    });
  }

  private static void backgroundRun(Runnable rb) {
    new Thread(rb).start();
  }

  public static void main(String[] args) {
    MainWin mainWin = new MainWin();
    mainWin.initUI();
    mainWin.mainFrame.setVisible(true);
  }

  private Set<String> getRecommandTransCities() {
    pendingQuery = grabUserInputParams();
    Set<String> transSites = AirlineQuery.queryMiddleSite(pendingQuery, 10);

    //    for (String str : transSites)
    //      logger.log(str);
    return transSites;
  }

  private void onInputChange(
      @NotNull DocumentEvent e, @NotNull JComboBox<String> box, boolean first) {
    if (!dropDownUpdateFlag.get(first)) {
      try {
        Document doc = e.getDocument();
        JTextComponent tc = (JTextComponent) (box.getEditor().getEditorComponent());
        //        String curText = doc.getText(0, doc.getLength());
        String curText = tc.getText();
        //        logger.log(String.format("Curtext = %s", curText));
        Set<Map.Entry<String, Integer>> fetched = graph.searchCity(curText);

        if (fetched.size() > 0) {
          if (fetched.contains(curText)) {
            tc.setText(curText);
            dropDownUpdateFlag.set(false, first);
          } else {
            Vector<String> modelData = new Vector<>();
            //            logger.log("----------");
            for (Map.Entry<String, Integer> entry : fetched) {
              //              logger.log(entry.getKey());
              modelData.add(entry.getKey());
            }
            //            logger.log("----------");
            dropDownUpdateFlag.set(true, first);
            box.setModel(new DefaultComboBoxModel<>(modelData));
          }
        }
        box.showPopup();
      } catch (Exception ep) {
        ep.printStackTrace();
      }
    } else {
      dropDownUpdateFlag.set(false, first);
    }
  }

  public void log(String line) {
    logAreaTA.append(line + "\n");
  }

  private void initUI() {
    mainFrame = new JFrame("InteliTicket智能购票推荐系统");

    UIUtils.enableComponents(queryPN, false);

    //    logAreaTA.setBorder(BorderFactory.createLineBorder(Color.black));
    mainFrame.setContentPane(rootPN);
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainFrame.pack();
  }

  private void configDateSP(@NotNull JSpinner sp) {
    SpinnerDateModel model = new SpinnerDateModel();
    sp.setModel(model);
    sp.setValue(new Date());
    JSpinner.DateEditor editor = new JSpinner.DateEditor(sp, "yyyy-MM-dd");
    sp.setEditor(editor);
  }

  @NotNull
  private QueryParamBean grabUserInputParams() {
    QueryParamBean params = new QueryParamBean();
    params.setGraph(graph);
    params.setSrc(srcCityTextCp.getText().trim().replaceAll("[^\\u4e00-\\u9fa5]+", ""));
    params.setDst(dstCityTextCp.getText().trim().replaceAll("[^\\u4e00-\\u9fa5]+", ""));
    params.setDate(TimeUnit.MILLISECONDS.toMinutes(((Date) dateSP.getValue()).getTime()));
    params.setTodayOnly(todayOnlyCBBX.isSelected());
    params.setAuto(autoSuggestCBX.isSelected());
    if (!params.isAuto()) {
      params.setTransNum(Integer.valueOf((String) transNumCBBX.getSelectedItem()));
      params.setDuration(
          TimeUnit.DAYS.toMinutes(Integer.valueOf((String) stayDaysCBBX.getSelectedItem()))
              + TimeUnit.HOURS.toMinutes(Integer.valueOf((String) stayHoursCBBX.getSelectedItem()))
              + Integer.valueOf((String) stayMinsCBBX.getSelectedItem()));
    }
    return params;
  }

  private void updateDateList(List<QueryResultBean> beans) {
    resultAreaPN.removeAll();
    for (JComponent component : ResultRowUICmp.build(beans, graph)) {
      resultAreaPN.add(component);
    }
    resultAreaPN.add(Box.createVerticalGlue());
    resultSPN.updateUI();
  }

  @Nullable
  private JComponent makeJCmpFromData(QueryResultBean data) {
    ResultRowUICmp rowUI = new ResultRowUICmp(graph);
    return rowUI.build(data);
  }

  private void createUIComponents() {
    dateSP = new JSpinner();
    configDateSP(dateSP);

    logAreaTA = new JTextArea(5, 50);
    logAreaTA.setLineWrap(true);
    //    logAreaTA.setEnabled(false);
    DefaultCaret caret = (DefaultCaret) logAreaTA.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    //    logSPN = new JScrollPane(logAreaTA);
    resultAreaPN = new JPanel();
    resultAreaPN.setLayout(new BoxLayout(resultAreaPN, BoxLayout.Y_AXIS));
    //    resultSPN = new JScrollPane(resultAreaPN);
    //    resultSPN.setPreferredSize(new Dimension(-1, 300));
  }

  private class InternalDoubleFlag {

    private boolean f1;
    private boolean f2;

    private void set(boolean val, boolean first) {
      if (first) {
        f1 = val;
      } else {
        f2 = val;
      }
    }

    private boolean get(boolean first) {
      return first ? f1 : f2;
    }
  }
}
