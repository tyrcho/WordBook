package info.daviot.dictionary

import info.daviot.gui.component.JTextField

import java.awt.Font
import java.nio.charset.Charset

object DictionaryConstants {
  val CHARSET = Charset.forName("UTF-8")
  val FONT = new JTextField().getFont().deriveFont(25f)
}
