package info.daviot.wordbook

import swing._
import info.daviot.wordbook.model.Translation
import javax.swing.table._
import TranslationTable._
import java.awt.GraphicsEnvironment
import java.awt.Font
import javax.swing.JTextField

class TranslationTable(translations:Seq[Translation]) extends Table{
  model=buildModel(translations)
  font=new JTextField().getFont()
  println(font.getName())

}

object TranslationTable {
  def columnNames(c:Int)=c match {
    case 0 => "Native"
    case 1 => "Other"
  }
  
  def buildModel(translations:Seq[Translation]):TableModel=new AbstractTableModel {
      override def getColumnName(column: Int) = columnNames(column).toString
      def getRowCount() = translations.length
      def getColumnCount() = 2
      def getValueAt(row: Int, col: Int): AnyRef = {
        val t=translations(row)
        col match {
          case 0 => t.native
          case 1 => t.other
      }
      
    }
  }
    
}