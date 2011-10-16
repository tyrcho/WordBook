package info.daviot.gui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Box;
import javax.swing.JButton;

class ErrorMessageDialog(frame: Frame, title: String, message: String, exception: Throwable = null) extends InformationDialog(frame, title) {

  override def setVisible(visible: Boolean) {
    super.setVisible(visible);
    if (visible)
      detailsButton.requestFocus();
  }

  val topBox = Box.createVerticalBox();
  val messageTextArea = new JTextArea(80, 5);
  messageTextArea.setLineWrap(true);
  messageTextArea.setText(message);
  messageTextArea.setEditable(false);
  // +System.getProperty("line.separator")+exception);
  val messageScrollPane = new JScrollPane(messageTextArea);
  messageScrollPane.setPreferredSize(new Dimension(800, 100));
  val detailsTextArea = new JTextArea(80, 20);
  detailsTextArea.setLineWrap(true);
  detailsTextArea.setEditable(false);
  detailsTextArea.setText(throwableToString(exception));
  val detailsScrollPane = new JScrollPane(detailsTextArea);
  detailsScrollPane.setPreferredSize(new Dimension(800, 500));
  detailsScrollPane.setVisible(false);
  val detailsButton = new JButton("Details>>");
  detailsButton.addActionListener(new ActionListener() {
    def actionPerformed(event: ActionEvent) {
      if (detailsScrollPane.isVisible()) {
        detailsScrollPane.setVisible(false);
        detailsButton.setText("Details>>");
        pack();
      } else {
        detailsScrollPane.scrollToBeginning();
        detailsScrollPane.setVisible(true);
        detailsButton.setText("Details<<");
        pack();
      }
    }
  });
  topBox.add(messageScrollPane);
  topBox.add(Box.createVerticalStrut(10));
  topBox.add(detailsButton);
  getInsidePanel().setLayout(new BorderLayout());
  getInsidePanel().add(topBox, BorderLayout.NORTH);
  getInsidePanel().add(detailsScrollPane, BorderLayout.CENTER);
  pack();
  addWindowListener(new WindowAdapter() {
    override def windowClosing(e: WindowEvent) {
      dispose();
    }
  });
  detailsButton.setNextFocusableComponent(getCloseButton());
  getCloseButton().setNextFocusableComponent(detailsButton);

  private def throwableToString(exception: Throwable): String = {
    if (exception == null)
      return "";
    val stringWriter = new StringWriter();
    val printWriter = new PrintWriter(stringWriter);
    exception.printStackTrace(printWriter);
    printWriter.flush();
    return stringWriter.toString();
  }
}
