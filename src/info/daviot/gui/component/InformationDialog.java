package info.daviot.gui.component;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Custom Dialog with a Close button. Modal, not resizable. The Close button disposes (closes) the Dialog.
 */
public class InformationDialog extends JDialog {
    private static final long serialVersionUID = 3402143316854239899L;
    private JPanel            mainPanel;
    protected ActionListener  controller;
    protected JButton         closeButton      = new JButton("Fermer");

    /**
     * Translates the title using resource <i>Titles</i>.
     */
    public InformationDialog(Frame frame, String title) {
        super(frame, title);
        setTitle(title);
        init();
    }

    /**
     * Translates the title using resource <i>Titles</i>.
     */
    public InformationDialog(Dialog parent, String title) {
        super(parent, title);
        setTitle(title);
        init();
    }

    private void init() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        controller = new ButtonsListener(this);
        closeButton.addActionListener(controller);
        JPanel buttons = new JPanel(new BorderLayout());
        javax.swing.Box buttonsBox = Box.createHorizontalBox();
        buttonsBox.add(Box.createGlue());
        buttonsBox.add(closeButton);
        buttons.add(buttonsBox, BorderLayout.CENTER);
        mainPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }

        });
    }

    public JPanel getInsidePanel() {
        return mainPanel;
    }

    private class ButtonsListener implements ActionListener {
        private InformationDialog parent;

        public ButtonsListener(InformationDialog parent) {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            parent.dispose();
        }
    }
}