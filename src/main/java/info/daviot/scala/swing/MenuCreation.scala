package info.daviot.scala.swing
import scala.swing.{ Menu, MenuItem, MenuBar, Action }

/**
 * Simplifies the creation of menu and menu bars.
 */
object MenuCreation {
  def item(name: String, fun: => Unit) = new MenuItem(Action(name)(fun))

  def menu(name: String, menuItems: MenuItem*) = new Menu(name) { menuItems foreach (contents += _) }

  def bar(m: Menu*) = new MenuBar { m foreach (contents += _) }
}