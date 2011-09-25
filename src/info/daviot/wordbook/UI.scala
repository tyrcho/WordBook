package info.daviot.wordbook
import swing._
import swing.event._
import info.daviot.scala.swing.MenuCreation._

object UI extends SimpleSwingApplication {
  override def top = new Frame {
    title = "WorkBook"
    menuBar = bar(
      menu("File",
        item("New", newBook),
        item("Exit", close)))
    reactions += {
      case WindowClosing(_) => sys.exit
    }
  }

  def newBook { println("new") }

}

