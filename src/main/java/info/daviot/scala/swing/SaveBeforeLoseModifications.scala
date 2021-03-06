package info.daviot.scala.swing
import java.io.File
import scala.swing._
import scala.swing.Dialog._

trait SaveBeforeLoseModifications {
  var modified: Boolean = false
  var optionPaneParent: Component = null

  /**
   * Tries to save,  true for success, false for failure.
   * Exception will no be handled by the trait.
   */
  def save: Boolean

  def checkAndSave(message: String = "Do you want to save before ?", popupTitle: String = "Confirm"): Boolean = {
    if (modified) {
      Dialog.showConfirmation(optionPaneParent, message, popupTitle, Options.YesNoCancel) match {
        case Result.Yes =>
          if (save) {
            modified = false
            true
          } else false
        case Result.No => true
        case _ => false
      }
    } else true
  }
}