package info.daviot.gui.component;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class OkCancelPanel extends JPanel {
    protected JButton okButton=new JButton("OK");
    protected JButton cancelButton=new JButton("Annuler");
    protected JPanel buttons;
    protected JPanel insidePanel;    

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public OkCancelPanel() {
        super(new BorderLayout());
        buttons=new JPanel(new BorderLayout());
        buttons.add(okButton, BorderLayout.WEST);
        buttons.add(cancelButton, BorderLayout.EAST);
        add(buttons, BorderLayout.SOUTH);
        insidePanel=new JPanel();
        add(insidePanel, BorderLayout.CENTER);
    }

    public JPanel getContentPanel() {
        return insidePanel;
    }

}
