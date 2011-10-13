package com.tyrcho.gui.toolkit;

import javax.swing.JInternalFrame;

public class InternalFrame extends JInternalFrame
{
   private static transient Toolkit toolkit = ToolkitFactory.getToolkit();

   public InternalFrame(
      String title,
      boolean resizable,
      boolean closable,
      boolean maximizable)
   {
      super(title, resizable, closable, maximizable);
      toolkit.initInternalFrame(this);
   }

   public InternalFrame(
      String title,
      boolean resizable,
      boolean closable,
      boolean maximizable,
      boolean iconifiable)
   {
      super(title, resizable, closable, maximizable, iconifiable);
      toolkit.initInternalFrame(this);
   }

   public InternalFrame(String title, boolean resizable, boolean closable)
   {
      super(title, resizable, closable);
      toolkit.initInternalFrame(this);
   }

   public InternalFrame(String title, boolean resizable)
   {
      super(title, resizable);
      toolkit.initInternalFrame(this);
   }

   public InternalFrame(String title)
   {
      super(title);
      toolkit.initInternalFrame(this);
   }

   public InternalFrame()
   {
      super();
      toolkit.initInternalFrame(this);
   }
}