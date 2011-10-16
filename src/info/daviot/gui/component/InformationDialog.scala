package info.daviot.gui.component;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing._

/**
 * Custom Dialog with a Close button. Modal, not resizable. The Close button disposes (closes) the Dialog.
 */
class InformationDialog(frame: Frame, title: String) extends JDialog(frame, title) {

  val mainPanel = new JPanel()
  val closeButton = new JButton("Fermer");

  setTitle(title);
  setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
  setResizable(false);
  val controller = new ActionListener() {

    def actionPerformed(e: ActionEvent) {
      InformationDialog.this.dispose();
    }
  }
  closeButton.addActionListener(controller);
  val buttons = new JPanel(new BorderLayout());
  val buttonsBox = Box.createHorizontalBox();
  buttonsBox.add(Box.createGlue());
  buttonsBox.add(closeButton);
  buttons.add(buttonsBox, BorderLayout.CENTER);
  getContentPane().add(mainPanel, BorderLayout.CENTER);
  getContentPane().add(buttons, BorderLayout.SOUTH);
  pack();
  addWindowListener(new WindowAdapter() {
    override def windowClosing(e: WindowEvent) {
      dispose();
    }
  });

  def getInsidePanel() = mainPanel
  def getCloseButton() = closeButton

}