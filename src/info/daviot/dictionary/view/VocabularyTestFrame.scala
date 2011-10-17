package info.daviot.dictionary.view;

import info.daviot.dictionary.DictionaryConstants;
import info.daviot.dictionary.model.DictionaryEntry;
import info.daviot.dictionary.model.Question;
import info.daviot.dictionary.model.Session;
import info.daviot.dictionary.model.SessionCompleteEvent;
import info.daviot.dictionary.model.SessionCompleteListener;
import info.daviot.gui.component.console.CommandEvent;
import info.daviot.gui.component.console.CommandEventListener;
import info.daviot.gui.component.console.ConsolePanel;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import scala.collection.JavaConversions._

class VocabularyTestFrame(title: String, session: Session) extends JFrame(title) {
  val listeners = new LinkedList[SessionCompleteListener]();
  var iterator: Iterator[Question] = _
  var currentQuestion: Question = _
  val listener = new CommandEventListener() {
    def commandPerformed(e: CommandEvent) {
      questionAnswered(e.getText().trim());
    }
  };
  val errors = new StringBuffer();

  val console = new ConsolePanel();
  console.setFont(DictionaryConstants.FONT);
  getContentPane().setLayout(new BorderLayout());
  getContentPane().add(console.getPanel(), BorderLayout.CENTER);

  def getErrors() = errors.toString();

  def addSessionCompleteListener(listener: SessionCompleteListener) { listeners.add(listener); }

  def removeSessionCompleteListener(listener: SessionCompleteListener) { listeners.remove(listener); }

  def fireSessionCompleteEvent(score: String) {
    val event = new SessionCompleteEvent(this, score);
    for (listener <- listeners) {
      listener.sessionComplete(event);
    }
  }

  def runSession() {
    pack();
    setVisible(true);
    console.clear();
    console.requestFocus();
    iterator = session.iterator();
    console.addCommandEventListener(listener);
    nextQuestion();
  }

  private def questionAnswered(answer: String) {
    currentQuestion.inputTranslation_$eq(answer);
    val dictionaryEntry = currentQuestion.dictionaryEntry
    if (currentQuestion.isAnswerValid()) {
      session.updateScore();
      dictionaryEntry.incrementGoodAnswers();
      dictionaryEntry.rating_$eq(session.newRating(
        dictionaryEntry.getRating(), true));
      // int goodAnswers = dictionaryEntry.goodAnswers();
      val score = dictionaryEntry.displayRating();
      // "("+goodAnswers+"/"+(goodAnswers+dictionaryEntry.getWrongAnswers())+")";
      console.println("Bravo " + score);
    } else {
      val possibleConfusion = session
        .getPossibleConfusion(answer.trim());
      if (possibleConfusion != null) {
        console.println(String.format("*** %s == %s (%s)***", answer,
          possibleConfusion.firstTranslation(),
          possibleConfusion.explaination));
        possibleConfusion.incrementWrongAnswers();
        possibleConfusion.rating_$eq(session.newRating(
          possibleConfusion.getRating(), false));

      }
      val translations = dictionaryEntry.translations.size();
      dictionaryEntry.incrementWrongAnswers();
      dictionaryEntry.rating_$eq(session.newRating(
        dictionaryEntry.getRating(), false));
      // int goodAnswers = dictionaryEntry.goodAnswers();
      // String
      // score="("+goodAnswers+"/"+(goodAnswers+dictionaryEntry.getWrongAnswers())+")";
      val score = dictionaryEntry.displayRating();
      val message = if (translations > 1) "les traductions possible pour "
      else "la bonne traduction pour " + currentQuestion.word + " : " + currentQuestion.getTranslation();
      errors.append(String.format("%s et non <%s> (%s)%n", message, answer, dictionaryEntry.explaination));
      console.println("*** ERREUR *** " + score + ", " + message);

    }
    val example = dictionaryEntry.explaination;
    if (example != null && example.trim() != "") {
      console.println(example);
    }
    console.println();
    nextQuestion();
  }

  private def nextQuestion() {
    if (iterator.hasNext()) {
      currentQuestion = iterator.next();
      console.println(currentQuestion.toString());
    } else {
      SwingUtilities.invokeLater(new Runnable() {
        def run() {
          console.removeCommandEventListener(listener);
          fireSessionCompleteEvent(session.getScore());
        }
      });
    }
  }

}
