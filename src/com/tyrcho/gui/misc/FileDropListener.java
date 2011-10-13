package com.tyrcho.gui.misc;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Facilitates the drop of a list of files to a component.
 * @see java.awt.Component.setDropTarget
 * @author MDA
 */
public abstract class FileDropListener implements DropTargetListener
{
        public void dragEnter (DropTargetDragEvent dropTargetDragEvent)
        {
          dropTargetDragEvent.acceptDrag (DnDConstants.ACTION_COPY_OR_MOVE);
        }
        
        public void dragExit (DropTargetEvent dropTargetEvent) {}
        public void dragOver (DropTargetDragEvent dropTargetDragEvent) {}
        public void dropActionChanged (DropTargetDragEvent dropTargetDragEvent){}
        
        
        protected abstract void processFile(File file);

        public synchronized void drop (DropTargetDropEvent dropTargetDropEvent)
        {
            try
            {
                Transferable tr = dropTargetDropEvent.getTransferable();
                if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor))
                {
                    dropTargetDropEvent.acceptDrop (
                        DnDConstants.ACTION_COPY_OR_MOVE);
                    java.util.List fileList = (java.util.List)
                        tr.getTransferData(DataFlavor.javaFileListFlavor);
                    Iterator iterator = fileList.iterator();
                    while (iterator.hasNext())
                    {
                      File file = (File)iterator.next();
                      processFile(file);
                    }
                    dropTargetDropEvent.getDropTargetContext().dropComplete(true);
              } else {
                System.err.println ("unsupported flavor : DataFlavor.javaFileListFlavor");
                dropTargetDropEvent.rejectDrop();
              }
            } catch (IOException io) {
                io.printStackTrace(System.err);
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace(System.err);
                dropTargetDropEvent.rejectDrop();
            }   
        }    

    public static void main(String s[])
    {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JLabel label=new JLabel("roro");
        frame.getContentPane().add(label);
		DropTarget dropTarget=new DropTarget(label,new FileDropListener()
		{
        	protected void processFile(File file)
        	{
        		System.out.println(file);
        	}
		});
        
        frame.pack();
        frame.setVisible(true);
    }

}
