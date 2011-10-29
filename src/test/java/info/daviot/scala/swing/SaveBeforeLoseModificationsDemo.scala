package info.daviot.scala.swing
import scala.swing._

object SaveBeforeLoseModificationsDemo extends SimpleSwingApplication with SaveBeforeLoseModifications {
  def top = new MainFrame {
    contents = new FlowPanel() {
      contents += Button("save")(modified = false)
      contents += Button("modify")(modified = true)
      contents += Button("check")(clicked)
    }
  }

  def save = if (util.Random.nextBoolean) saveOK else saveKO

  def saveOK = {
    Dialog.showMessage(null, "save OK")
    true
  }

  def saveKO = {
    Dialog.showMessage(null, "saving failed")
    false
  }

  def clicked { Dialog.showMessage(null, checkAndSave()) }
}