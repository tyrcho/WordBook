package info.daviot.dictionary.model;

import junit.framework.TestCase
import org.junit.Before
import org.junit.Assert._
import org.junit.Test

class TwoWayDictionnaryTest {

  val SALUT_A_TOUS = "Salut � tous";
  val HELLO_WORLD = "Hello, world";
  val SALUT = "salut";
  val HELLO = "hello";
  val BONJOUR = "bonjour";

  var dictionnary: TwoWayDictionary = _

  @Before
  def setUp() {
    dictionnary = new TwoWayDictionary("francais", "anglais");
    dictionnary.addTranslation(SALUT, HELLO);
    dictionnary.addTranslation(BONJOUR, HELLO);
    dictionnary.addSecondLanguageExplaination(HELLO, HELLO_WORLD);
    dictionnary.addFirstLanguageExplaination(SALUT, SALUT_A_TOUS);
  }

  @Test
  def testFirstGetter() {
    assertTrue(dictionnary.getFirstLanguageEntry(BONJOUR).translations.contains(HELLO));
    assertTrue(dictionnary.getFirstLanguageEntry(SALUT).translations.contains(HELLO));
  }

  @Test
  def testSecondGetter() {
    assertTrue(dictionnary.getSecondLanguageEntry(HELLO).translations.contains(BONJOUR));
    assertTrue(dictionnary.getSecondLanguageEntry(HELLO).translations.contains(SALUT));
  }

  @Test
  def testexplaination {
    assertEquals(HELLO_WORLD, dictionnary.getSecondLanguageEntry(HELLO).explaination);
    assertEquals(SALUT_A_TOUS, dictionnary.getFirstLanguageEntry(SALUT).explaination);
  }

  @Test
  def testRemove() {
    dictionnary.removeFirstLanguageWord(SALUT);
    assertTrue(dictionnary.getFirstLanguageEntry(BONJOUR).translations.contains(HELLO));
    assertNull(dictionnary.getFirstLanguageEntry(SALUT));
    assertTrue(dictionnary.getSecondLanguageEntry(HELLO).translations.contains(BONJOUR));
    assertFalse(dictionnary.getSecondLanguageEntry(HELLO).translations.contains(SALUT));
  }
}
