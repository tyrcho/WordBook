package com.tyrcho.gui.toolkit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.tyrcho.util.language.Translator;
import com.tyrcho.util.language.TranslatorFactory;

/**
 * Custom component which displays 2 lists of items : the left one contains
 * available ones, the right one contains selected ones.
 * 
 * @author MDA
 * @version NP
 */
public class DoubleListFiller extends LabeledPanel
{
   protected List availableList;
   protected List chosenList;
   protected Vector hiddenItems=new Vector();
   protected TextButton chooseSelectionButton;
   protected TextButton unchooseSelectionButton;
   protected TextButton chooseAllButton;
   protected TextButton unchooseAllButton;
   protected Dimension buttonSize = new Dimension(25, 15);
   protected DoubleListFillerController controller;
   private static final Dimension listSize = new Dimension(180, 250);
   private static Translator translator=TranslatorFactory.getTranslator();
   private ScrollPane leftScrollPane;
   private ScrollPane rightScrollPane;
   private static transient Toolkit toolkit = ToolkitFactory.getToolkit();

   /**
    * Builds the DoubleListFiller with only available items.
    * 
    * @param title the panel title
    * @param availableData the available items
    */
   public DoubleListFiller(String title, Object[] availableData)
   {
   	  this(title, availableData, new Object[0]);
   }  

   /**
    * Builds the DoubleListFiller with a list of available items and a list of chosen items.
    * Note : an item should not be present in both lists.
    * 
    * @param title the panel title
    * @param availableData the available items
    * @param availableData the chosen items
    */
   public DoubleListFiller(String title, Object[] availableData, Object[] chosenData)
   {
      super(title);
      controller = new DoubleListFillerController(this);
      setLayout(new BorderLayout());
      javax.swing.Box mainPanel = Box.createHorizontalBox();
      availableList = new List(new ListModel(availableData));
      availableList.setVisibleRowCount(10);
      availableList.setSelectedIndex(0);
      availableList.addMouseListener(controller);
      chosenList = new List(new ListModel(chosenData));
      chosenList.setVisibleRowCount(10);
      chosenList.addMouseListener(controller);
      leftScrollPane = new ScrollPane(availableList);
      rightScrollPane = new ScrollPane(chosenList);
      rightScrollPane.setPreferredSize(listSize);
      leftScrollPane.setPreferredSize(listSize);
      javax.swing.Box buttonsPanel = Box.createVerticalBox();
      chooseAllButton = constructButton(">>", translator.getString("Add_all", "Components"));
      chooseSelectionButton = constructButton(">", translator.getString("Add", "Components"));
      unchooseSelectionButton = constructButton("<", translator.getString("Remove", "Components"));
      unchooseAllButton = constructButton("<<", translator.getString("Remove_all", "Components"));
      buttonsPanel.add(chooseAllButton);
      buttonsPanel.add(Box.createGlue());
      buttonsPanel.add(chooseSelectionButton);
      buttonsPanel.add(Box.createGlue());
      buttonsPanel.add(unchooseSelectionButton);
      buttonsPanel.add(Box.createGlue());
      buttonsPanel.add(unchooseAllButton);
      mainPanel.add(leftScrollPane);
      mainPanel.add(Box.createGlue());
      mainPanel.add(buttonsPanel);
      mainPanel.add(Box.createGlue());
      mainPanel.add(rightScrollPane);

      Panel labels = new Panel(new BorderLayout());
      labels.add(new Label(translator.getString("available", "Components")), BorderLayout.WEST);
      labels.add(new Label(translator.getString("chosen", "Components")), BorderLayout.EAST);
      add(mainPanel, BorderLayout.CENTER);
      add(labels, BorderLayout.NORTH);
      toolkit.initDoubleListFiller(this);
   }

   public List getChosenList()
   {
      return chosenList;
   }

   public List getAvailableList()
   {
      return availableList;
   }

	/**
	 * Hides an item from the available list.
	 */
	public void hideItem(Object item)
	{
		ListModel model = (ListModel)getAvailableList().getModel();
		if (model.contains(item))
		{
			model.removeElement(item);
			hiddenItems.add(item);
		}
	}

	/**
	 * Reveals an item in the available list that was previously hidden.
	 */
	public void revealItem(Object item)
	{
		if (hiddenItems.contains(item))
		{
			ListModel model = (ListModel)getAvailableList().getModel();
			model.addElement(item);
			hiddenItems.remove(item);
		}		
	}

	/**
	 * Reveals all items in the available list that were previously hidden.
	 */
	public void revealAll()
	{
		for (int i=hiddenItems.size()-1; i>=0; i--)
			revealItem(hiddenItems.get(i));
	}

   /**
    * Moves an item from the available list to the chosen list.
    * Does nothing if the item doesn't exist in the available list.
    * 
    * @param item the item to move as an Object
    */
   public void chooseItem(Object item)
   {
      ListModel inputModel = (ListModel)getAvailableList().getModel();
      ListModel outputModel = (ListModel)getChosenList().getModel();
      if (inputModel.contains(item))
      {
	      outputModel.addElement(item);
 	      inputModel.removeElement(item);
      }
   }

   /**
    * Moves an item from the chosen list to the available list.
    * Does nothing if the item doesn't exist in the chosen list.
    * 
    * @param item the item to move as an Object
    */
   public void unchooseItem(Object item)
   {
      ListModel inputModel = (ListModel)getAvailableList().getModel();
      ListModel outputModel = (ListModel)getChosenList().getModel();
      if (outputModel.contains(item))
      {
	      inputModel.addElement(item);
 	      outputModel.removeElement(item);
      }
   }
   
   /**
    * Moves all the items from the chosen list to the available list.
    */
   public void unchooseAll()
   {
   	   chosenList.selectAll();
   	   unchooseSelection();
   }
   
   /**
    * Moves all the items from the available list to the chosen list.
    */
   public void chooseAll()
   {
       availableList.selectAll();
       chooseSelection();
   }
   
   /**
    * Moves the selected items from the chosen list to the available list.
    */
   public void unchooseSelection()
   {
   		if (chosenList.getModel().getSize() > 0)
   		{
	   		Object[] selection=chosenList.getSelectedValues();
	   		for (int i=0; i<selection.length;i++)
	   			unchooseItem(selection[i]);
   		}
   }
   
   /**
    * Moves the selected items from the available list to the chosen list.
    */
   public void chooseSelection()
   {
   		if (availableList.getModel().getSize() > 0)
   		{
	   		Object[] selection=availableList.getSelectedValues();
	   		for (int i=0; i<selection.length;i++)
	   			chooseItem(selection[i]);
   		}
   			/*
         int[] selectedIndices = availableList.getSelectedIndices();
         DefaultListModel toModel = availableList.getModel();
         DefaultListModel fromModel = chosenList.getModel();

         for (int i = 0; i < selectedIndices.length; i++)
            {
            int selection = selectedIndices[i];
            toModel.addElement(fromModel.getElementAt(selection));
         }
         for (int i = selectedIndices.length - 1; i >= 0; i--)
            {
            int selection = selectedIndices[i];
            fromModel.removeElementAt(selection);
         }
         */
   }
   
   /**
    * Returns the items in the chosen list.
    * 
    * @return an array with the items
    */
   public Object[] getChosenItems()
   {
      ListModel outputModel = (ListModel)getChosenList().getModel();
   		return outputModel.toArray();
   }

   /**
    * Returns the items in the available list.
    * 
    * @return an array with the items
    */
   public Object[] getAvailableItems()
   {
      ListModel outputModel = (ListModel)getAvailableList().getModel();
   		return outputModel.toArray();
   }

   private TextButton constructButton(String label, String toolTip)
   {
      TextButton button = new TextButton(label);
      button.setActionCommand(label);
      button.addActionListener(controller);
      button.setToolTipText(toolTip);
      button.setMaximumSize(buttonSize);
      button.setPreferredSize(buttonSize);
      return button;
   }
   
     /* private void moveSelection(List fromList, List toList)
      {
         int[] selectedIndices = fromList.getSelectedIndices();
         DefaultListModel toModel = copy(toList.getModel());
         DefaultListModel fromModel = copy(fromList.getModel());

         for (int i = 0; i < selectedIndices.length; i++)
            {
            int selection = selectedIndices[i];
            toModel.addElement(fromModel.getElementAt(selection));
         }
         for (int i = selectedIndices.length - 1; i >= 0; i--)
            {
            int selection = selectedIndices[i];
            fromModel.removeElementAt(selection);
         }
         fromList.setModel(fromModel);
         fromList.setSelectedIndex(0);
         toList.setModel(toModel);
      }

      private void moveAll(List fromList, List toList)
      {
         DefaultListModel toModel = copy(toList.getModel());
         DefaultListModel fromModel = copy(fromList.getModel());

         for (int i = 0; i < fromModel.getSize(); i++)
            {
            toModel.addElement(fromModel.getElementAt(i));
         }
         for (int i = fromModel.getSize() - 1; i >= 0; i--)
            {
            fromModel.removeElementAt(i);
         }
         fromList.setModel(fromModel);
         toList.setModel(toModel);
         toList.setSelectedIndex(0);
      }
      */

   private class DoubleListFillerController extends MouseAdapter implements ActionListener
   {
      private DoubleListFiller filler;

      public DoubleListFillerController(DoubleListFiller doubleListFiller)
      {
         this.filler = doubleListFiller;
      }
      
	public void mouseClicked(MouseEvent event) {
         if (event.getClickCount() == 2) 
         {
         	 JList clickedList=(JList) event.getSource();
             int index = clickedList.locationToIndex(event.getPoint());
             try
             {
	             Object item=clickedList.getModel().getElementAt(index);
	             if (clickedList.equals(availableList))
	             	chooseSelection();
	             else
	             	unchooseSelection(); 
             }            
             catch (ArrayIndexOutOfBoundsException e)
             {}
          }
     }
     
      public void actionPerformed(ActionEvent e)
      {
         String action = e.getActionCommand();
         if (action.equals(chooseAllButton.getActionCommand()))
            chooseAll();
         else if (action.equals(unchooseAllButton.getActionCommand()))
            unchooseAll();
         else if (action.equals(unchooseSelectionButton.getActionCommand()))
         {
            unchooseSelection();
            chosenList.setSelectedIndex(0);
         }
         else if (action.equals(chooseSelectionButton.getActionCommand()))
         {
            chooseSelection();
            availableList.setSelectedIndex(0);
         }
      }

   }

	private class ListModel extends DefaultListModel
	{
		public ListModel(Object[] data)
		{
			super();
			for (int i=0; i<data.length; i++)
				addElement(data[i]);
		}
		
		/**
		 * Copy constructor.
		 */
		public ListModel(DefaultListModel model)
		{
			super();
			Enumeration elements=model.elements();
			while (elements.hasMoreElements())
				addElement(elements.nextElement());
		}
	}

	public ScrollPane getLeftScrollPane() 
	{
		return leftScrollPane;
	}

	public ScrollPane getRightScrollPane() 
	{
		return rightScrollPane;
	}
}
