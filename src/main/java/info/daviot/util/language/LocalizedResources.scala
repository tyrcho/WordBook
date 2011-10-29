package info.daviot.util.language

import java.util.Locale
import java.util.ResourceBundle

class LocalizedResources(key: String) {
  import LocalizedResources._

  def getString(file: String = defaultFileName, locale: Locale = Locale.getDefault): String =
    ResourceBundle.getBundle(file, locale).getString(key)

  /**
   * Localized version of string.
   */
  def l = getString()

}
object LocalizedResources {
  var defaultFileName: String = _
  implicit def asLocalizedResources(s: String) = new LocalizedResources(s)
}