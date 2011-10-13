package info.daviot.gui.toolkit;

import info.daviot.gui.toolkit.calendar.Spinner;
import info.daviot.gui.toolkit.calendar.SpinnerComboBox;

import java.awt.Component;


/**
 * Initializes the components according to the selected toolkit (implementation).
 * 
 * @author  BDE
 * @version NP
 */
public interface Toolkit
{ 
   public void initRadioButton(RadioButton radioButton);
   public void initTabbedPane(TabbedPane tabbedPane);
   public void initMenu(Menu menu);
   public void initMenuItem(MenuItem menuItem);
   public void initMenuBar(MenuBar menuBar);
   public void initFrame(Frame frame);
   public void initInternalFrame(InternalFrame frame);
   public void initDialog(Dialog dialog);
   public void initLabel(Label label);
   public void initPanel(Panel panel);
   public void initBox(javax.swing.Box box);
   public void initGlue(Component glue);
   public void initLabeledPanel(LabeledPanel panel);
   public void initTable(Table table);
   public void initTableCellRenderer(Component tableCellRenderer);
   public void initList(List list);
   public void initComboBox(ComboBox comboBox);
   public void initCheckBox(CheckBox checkBox);
   public void initScrollPane(ScrollPane scrollPane);
   public void initTextField(TextField textField);
   public void initTextButton(TextButton textButton);
   public void initImageButton(ImageButton imageButton);
   public void initImageLabel(ImageLabel imageLabel);
   public void initTextArea(TextArea textArea);
   public void initSplitPane(SplitPane splitPane);
   public void initDoubleListFiller(DoubleListFiller doubleListFiller);
   public void initSpinner(Spinner spinner);
   public void initSpinnerComboBox(SpinnerComboBox spinnerComboBox);
}