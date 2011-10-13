package com.tyrcho.gui.toolkit;

import javax.swing.ListModel;

public class List extends javax.swing.JList
{
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public List()
	{
		super() ;
		toolkit.initList(this) ;
	}

	public List(ListModel model)
	{
		super(model) ;
		toolkit.initList(this) ;
	}

	public List(Object[] rowData) {
		super(rowData);
		toolkit.initList(this) ;
	}
	
	public void selectAll()
	{
		getSelectionModel().setSelectionInterval(0,getModel().getSize()-1);
	}
}
