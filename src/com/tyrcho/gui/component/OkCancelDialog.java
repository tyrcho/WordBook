package com.tyrcho.gui.component;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.tyrcho.gui.toolkit.Dialog;

/**
 * Displays a dialog with OK and Cancel buttons.
 * When the dialog is closed, a click on cancel is simulated.
 * 
 * @author tyrcho
 */
@SuppressWarnings("serial")
public class OkCancelDialog extends JDialog{
    private OkCancelPanel mainPanel;
    
    public OkCancelDialog(Frame owner, String title) {
        super(owner, title);
        setTitle(title);
        init();
    }

    public OkCancelDialog(Dialog owner, String title) {
        super(owner, title);
        setTitle(title);
        init();
    }
    
    

    private void init()
    {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        mainPanel=new OkCancelPanel();
        add(mainPanel, BorderLayout.CENTER);
        getRootPane().setDefaultButton(mainPanel.getOkButton());
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                mainPanel.getCancelButton().doClick();                
            }
        });
        pack();
    }

    /**
     * Returns the inside panel which should be used to add components to this NpDialog.
     */
    public JPanel getInsidePanel() {
        return mainPanel.getContentPanel();
    }

    public JButton getCancelButton() {
        return mainPanel.getCancelButton();
    }

    public JButton getOkButton() {
        return mainPanel.getOkButton();
    }
}