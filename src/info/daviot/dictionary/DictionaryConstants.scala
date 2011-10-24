package info.daviot.dictionary

import java.awt.Font
import java.nio.charset.Charset
import javax.swing.JTextField

object DictionaryConstants {
  val CHARSET = Charset.forName("UTF-8")
  val FONT = new JTextField().getFont().deriveFont(25f)
}
