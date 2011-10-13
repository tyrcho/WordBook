package info.daviot.gui.toolkit;

import java.awt.Component;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class Table extends javax.swing.JTable
{
	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public Table()
	{
		super() ;
		toolkit.initTable(this) ;
	}

	public Table(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		toolkit.initTable(this) ;
	}
	
	public Table(TableModel tableModel)
	{
		super(tableModel);
		toolkit.initTable(this) ;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		toolkit.initTableCellRenderer(c);
		return c;
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		TableCellRenderer renderer= super.getCellRenderer(row, column);
		//toolkit.initTableCellRenderer(renderer.getTableCellRendererComponent());
		return renderer;
	}


}
