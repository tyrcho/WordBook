package info.daviot.wordbook
import swing._
import swing.event._
import swing.BorderPanel.Position._
import info.daviot.scala.swing.MenuCreation._
import info.daviot.wordbook.model.Translation
import javax.swing.UIManager

object UI extends SimpleSwingApplication {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  
  override def top = new Frame {
    title = "WorkBook"
    menuBar = bar(
      menu("File",
        item("New", newBook),
        item("Exit", close)))
    reactions += {
      case WindowClosing(_) =>
        sys.exit
    }
    contents = new BorderPanel {
    	add(new TranslationTable(Seq(new Translation("hello", "你好"))),Center )
    }
  }

  def newBook { println("new") }

}

