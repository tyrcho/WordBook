package com.tyrcho.dictionary;

import java.awt.Font;
import java.nio.charset.Charset;

import com.tyrcho.gui.component.JTextField;

public class DictionaryConstants {

	public final static Charset CHARSET = Charset.forName("UTF-8");
	
	public final static Font FONT=new JTextField().getFont().deriveFont(25f);
}
