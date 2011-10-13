import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.sun.java.util.SpringUtilities;
import com.tyrcho.gui.component.JScrollPane;
import com.tyrcho.gui.component.SelectCategoriesPanel;

public class TestSpring {

public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel=new JPanel(new SpringLayout());
    for(int i=0; i<20; i++) {
        panel.add(new JComboBox());
        panel.add(new JButton("X"));
    }
    SpringUtilities.makeCompactGrid(panel, panel.getComponentCount()/2, 2);
    frame.getContentPane().add(new JScrollPane(panel));
    frame.pack();
    frame.setVisible(true);
}

}
