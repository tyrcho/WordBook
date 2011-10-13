package info.daviot.gui.toolkit;

import java.awt.Component;

public class Dialog extends javax.swing.JDialog
{
   private static transient Toolkit toolkit = ToolkitFactory.getToolkit();

   public Dialog(java.awt.Frame owner, String title)
   {
      super(owner, title, true);
      toolkit.initDialog(this);
   }

   public Dialog(java.awt.Dialog owner, String title)
   {
      super(owner, title, true);
      toolkit.initDialog(this);
   }

   /**
    * Show the initialized dialog.  The first argument should
    * be null if you want the dialog to come up in the center
    * of the screen.  Otherwise, the argument should be the
    * component on top of which the dialog should appear.
    */
   public void show(Component comp)
   {
      setLocationRelativeTo(comp);
      setVisible(true);
   }
}
