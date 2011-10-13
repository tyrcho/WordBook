package info.daviot.gui.toolkit.calendar;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * Makes the link between two components : a simple editor
 * such as a TextField and a popup editor which is displayed 
 * if the user presses a button.
 * The two components share the same model.
 * This is analog to a JComboBox but the popup can be any JPanel component
 * and does not display a list of items.
 *  
 * @author MDA
 * @version NP
 */
public class PopupEditor extends info.daviot.gui.toolkit.Panel
{
   private JWindow popupWindow;
   protected ComboBoxEditor simpleEditor;
   protected ComboBoxEditor popupEditor;
   private Collection changeListeners = new LinkedList();
   private BasicArrowButton button=new BasicArrowButton(BasicArrowButton.SOUTH);;

   /**
    * Constructs the popup editor.
    * 
    * @param simpleEditor the editor to use in the default appearance
    * @param popupEditor the editor to use 
    * when the button is pressed to display the popup
    * @param initialValue
    */
   public PopupEditor(
      ComboBoxEditor simpleEditor,
      ComboBoxEditor popupEditor,
      Object initialValue)
   {
      super(new BorderLayout());
      this.simpleEditor=simpleEditor;
      this.popupEditor=popupEditor;
      setValue(initialValue);
      init();
      add(simpleEditor.getEditorComponent(), BorderLayout.CENTER);
      add(button, BorderLayout.EAST);    
/*
      javax.swing.SwingUtilities.invokeLater(new Runnable()
      {
      	public void run()
      	{
      		showPopup();
      		hidePopup();
      	}
      });
*/
   }
   
   /**
    * Dispatches correctly the enable property.
    * 
    * @param enable if the value can be changed
    */
   public void setEnabled(boolean enable)
   {
   	  super.setEnabled(enable)  ;
   	  button.setEnabled(enable);
   	  simpleEditor.getEditorComponent().setEnabled(enable);
   }
   
   /**
    * Defines the model.
    * 
    * @param value the new model
    */
   public void setValue(Object value)
   {
      simpleEditor.setItem(value);
      popupEditor.setItem(value);
   }
   
   public void clear()
   {
   	  simpleEditor.setItem("");
   }
   
   /**
    * Gets the model.
    * 
    * @return the selected model in the simple editor
    */
   public Object getValue()
   {
   	  return simpleEditor.getItem();
   }
   
   private void init()
   {
   	  simpleEditor.getEditorComponent().addFocusListener(new FocusAdapter()
   	  {
         public void focusGained(FocusEvent e)
         {
            hidePopup();
         }   	  	 
   	  });
   	  popupEditor.getEditorComponent().addFocusListener(new FocusAdapter()
   	  {
         public void focusLost(FocusEvent e)
         {
            hidePopup();
         }   	  	 
   	  });
   	  simpleEditor.addActionListener(new ActionListener()
   	  {
   	  	 public void actionPerformed(ActionEvent e)
   	  	 {
   	  	 	 Object model=simpleEditor.getItem();
	      	 popupEditor.setItem(model);
             fireChangeEvent();
   	  	 }
   	  }); 
   	  popupEditor.addActionListener(new ActionListener()
   	  {
   	  	 public void actionPerformed(ActionEvent e)
   	  	 {
		    Object model=popupEditor.getItem();
		    simpleEditor.setItem(model);
            fireChangeEvent();
   	  	 }
   	  }); 
   	  button.addActionListener(new ActionListener()
   	  {
	     public void actionPerformed(ActionEvent e) 
	     {
	        if ((popupWindow != null) && (popupWindow.isVisible())) 
	        {
	            hidePopup();
	        } else {
	            showPopup();
	        }
	    }
   	  });
   }

   private void createPopupWindow()
   {
      JComponent ancestor = (JComponent) getRootPane().getContentPane();

      popupWindow = new JWindow();

      JPanel contentPanel = (JPanel) popupWindow.getContentPane();
      contentPanel.setBorder(BorderFactory.createEtchedBorder());
      contentPanel.setLayout(new BorderLayout());
      contentPanel.add(popupEditor.getEditorComponent(), BorderLayout.CENTER);

      ancestor.addAncestorListener(new AncestorListener()
      {
         public void ancestorAdded(AncestorEvent event)
         {
            hidePopup();
         }

         public void ancestorMoved(AncestorEvent event)
         {
            hidePopup();
         }

         public void ancestorRemoved(AncestorEvent event)
         {
            hidePopup();
         }
      });
      ancestor.addMouseListener(new MouseAdapter()
      {
         public void mouseClicked(MouseEvent e)
         {
            hidePopup();
         }
      });

      popupWindow.addWindowListener(new WindowAdapter()
      {

         public void windowDeactivated(WindowEvent e)
         {
            hidePopup();
         }
      });
/*
      popupWindow.addFocusListener(new FocusAdapter()
      {
         public void focusLost(FocusEvent e)
         {
            hidePopup();
         }
      });
      ancestor.addComponentListener(new ComponentAdapter()
      {

         public void componentResized(ComponentEvent e)
         {
            hidePopup();
         }

         public void componentMoved(ComponentEvent e)
         {
            hidePopup();
         }

         public void componentShown(ComponentEvent e)
         {
            hidePopup();
         }

         public void componentHidden(ComponentEvent e)
         {
            hidePopup();
         }
      });
*/
   }
   
   
/*   
   private void createPopupWindow()
   {
      Window ancestor = (Window) this.getTopLevelAncestor();

      popupWindow = new JWindow(ancestor);

      JPanel contentPanel = (JPanel) popupWindow.getContentPane();
      contentPanel.setBorder(BorderFactory.createEtchedBorder());
      contentPanel.setLayout(new BorderLayout());
      contentPanel.add(popupEditor.getEditorComponent(), BorderLayout.CENTER);
      RootPaneContainer rootContainer = (RootPaneContainer) ancestor;
      JComponent rootContentPane = (JComponent) rootContainer.getContentPane();
      rootContentPane.addAncestorListener(new AncestorListener()
      {
         public void ancestorAdded(AncestorEvent event)
         {
            hidePopup();
         }

         public void ancestorMoved(AncestorEvent event)
         {
            hidePopup();
         }

         public void ancestorRemoved(AncestorEvent event)
         {
            hidePopup();
         }
      });
      rootContentPane.addMouseListener(new MouseAdapter()
      {
         public void mouseClicked(MouseEvent e)
         {
            hidePopup();
         }
      });

      popupWindow.addWindowListener(new WindowAdapter()
      {

         public void windowDeactivated(WindowEvent e)
         {
            hidePopup();
         }
      });

      popupWindow.addFocusListener(new FocusAdapter()
      {
         public void focusLost(FocusEvent e)
         {
            hidePopup();
         }
      });
      ancestor.addComponentListener(new ComponentAdapter()
      {

         public void componentResized(ComponentEvent e)
         {
            hidePopup();
         }

         public void componentMoved(ComponentEvent e)
         {
            hidePopup();
         }

         public void componentShown(ComponentEvent e)
         {
            hidePopup();
         }

         public void componentHidden(ComponentEvent e)
         {
            hidePopup();
         }
      });
   }
*/   
   /**
    * Add a listener to receive change events when the edited value changes.
    * 
    * @param l the listener to receive events
    */
   public void addChangeListener(ChangeListener l)
   {
      changeListeners.add(l);
   }

   /**
    * Removes a change listener.
    * 
    * @param l the listener being removed	
    */
   public void removeChangeListener(ChangeListener l)
   {
      changeListeners.remove(l);
   }
   
   private void fireChangeEvent()
   {
   	  Iterator i=changeListeners.iterator();
   	  ChangeEvent event=new ChangeEvent(this);
   	  while (i.hasNext())
   	  {
   	  	 ChangeListener listener=(ChangeListener)i.next();
   	  	 listener.stateChanged(event);
   	  }
   }


   public void hidePopup()
   {
      if (popupWindow!=null)
      {
         popupWindow.setVisible(false);
      }
   }

   public void showPopup()
   {
   	  if(popupWindow==null)
   	  {
	   	  createPopupWindow();
   	  }
      popupWindow.pack();
      Window ancestor = (Window) this.getTopLevelAncestor();
      Point location = getLocationOnScreen();

      int x;

/*      if (popupLocation == RIGHT)
         {
         x =
            (int) location.getX()
               + button.getSize().width
               - popupWindow.getSize().width;
      }
      else if (popupLocation == CENTER)
         {
         x =
            (int) location.getX()
               + ((button.getSize().width - popupWindow.getSize().width) / 2);
      }
      else
      */
      {
         x = (int) location.getX();
      }

      int y = (int) location.getY() + button.getHeight();

      Rectangle screenSize = getDesktopBounds();

      if (x < 0)
      {
         x = 0;
      }

      if (y < 0)
      {
         y = 0;
      }

      if (x + popupWindow.getWidth() > screenSize.width)
      {
         x = screenSize.width - popupWindow.getWidth();
      }

      if (y + 30 + popupWindow.getHeight() > screenSize.height)
      {
         y = (int) location.getY() - popupWindow.getHeight();
      }

      popupWindow.setBounds(
         x,
         y,
         popupWindow.getWidth(),
         popupWindow.getHeight());
      popupWindow.setVisible(true);
   }

   /**
    * Gets the screensize. Takes into account multi-screen displays.
    * 
    * @return a union of the bounds of the various screen devices present
    */
   private Rectangle getDesktopBounds()
   {
      final GraphicsEnvironment ge =
         GraphicsEnvironment.getLocalGraphicsEnvironment();
      final GraphicsDevice[] gd = ge.getScreenDevices();
      final Rectangle[] screenDeviceBounds = new Rectangle[gd.length];
      Rectangle desktopBounds = new Rectangle();
      for (int i = 0; i < gd.length; i++)
      {
         final GraphicsConfiguration gc = gd[i].getDefaultConfiguration();
         screenDeviceBounds[i] = gc.getBounds();
         desktopBounds = desktopBounds.union(screenDeviceBounds[i]);
      }

      return desktopBounds;
   }

	public static void main(String[] s) 
	{
		JFrame frame = new JFrame("PopupEditor");
		PopupEditor chooser=new PopupEditor(new BasicComboBoxEditor(), new BasicComboBoxEditor(), "Toto");
		frame.getContentPane().add(chooser);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Gets the popupEditor
	 * @return Returns a ComboBoxEditor
	 */
	public ComboBoxEditor getPopupEditor() {
		return popupEditor;
	}
   /**
    * Sets the popupEditor
    * @param popupEditor The popupEditor to set
    */
   public void setPopupEditor(ComboBoxEditor popupEditor)
   {
      this.popupEditor = popupEditor;
   }

	/**
	 * Gets the simpleEditor
	 * @return Returns a ComboBoxEditor
	 */
	public ComboBoxEditor getSimpleEditor() {
		return simpleEditor;
	}
   /**
    * Sets the simpleEditor
    * @param simpleEditor The simpleEditor to set
    */
   public void setSimpleEditor(ComboBoxEditor simpleEditor)
   {
      this.simpleEditor = simpleEditor;
   }

}