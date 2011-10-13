package info.daviot.dictionary;

import info.daviot.gui.component.JTextField;

import java.awt.Font;
import java.nio.charset.Charset;


public class DictionaryConstants {

	public final static Charset CHARSET = Charset.forName("UTF-8");
	
	public final static Font FONT=new JTextField().getFont().deriveFont(25f);
}
