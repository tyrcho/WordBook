package info.daviot.gui.toolkit;

import java.awt.Component;

public class Box extends javax.swing.Box {

	private static transient Toolkit toolkit = ToolkitFactory.getToolkit() ;

	public Box(int axis)
	{
		super(axis) ;
		toolkit.initBox(this) ;
	}

	public static javax.swing.Box createVerticalBox() {
		javax.swing.Box box=javax.swing.Box.createVerticalBox();
		toolkit.initBox(box);
		return box;
	}

	public static javax.swing.Box createHorizontalBox() {
		javax.swing.Box box=javax.swing.Box.createHorizontalBox();
		toolkit.initBox(box);
		return box;
	}
	
	public static Component createHorizontalGlue() {
		Component glue=javax.swing.Box.createHorizontalGlue();
		toolkit.initGlue(glue);
		return glue;
	}

	public static Component createVerticalGlue() {
		Component glue=javax.swing.Box.createVerticalGlue();
		toolkit.initGlue(glue);
		return glue;
	}

	public static Component createGlue() {
		Component glue=javax.swing.Box.createGlue();
		toolkit.initGlue(glue);
		return glue;
	}

	public static Component createHorizontalStrut(int width) {
		Component box=javax.swing.Box.createHorizontalStrut(width);
		toolkit.initGlue(box);
		return box;
	}

	public static Component createVerticalStrut(int heigth) {
		Component box=javax.swing.Box.createVerticalStrut(heigth);
		toolkit.initGlue(box);
		return box;
	}
}

