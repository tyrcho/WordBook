package info.daviot.dictionary;

import org.junit.Test;
import org.junit.Assert._

class UtilsTest {
  @Test
  def testSimpleCompare() {
    assertTrue(Utils.simpleCompare("a1z233e", "aze", "223 1"));
    assertTrue(Utils.simpleCompare("aze", "aze", null));
    assertTrue(Utils.simpleCompare("a1z233e", "12a3ze22 ", "223 1"));
    assertFalse(Utils.simpleCompare("a1z233e", "12a3ze22 ", "3 1"));
    assertFalse(Utils.simpleCompare("a1z233e", "12a3ze22 ", ""));
    assertTrue(Utils.simpleCompare("aze", "aze", "555321"));
    assertTrue(Utils.simpleCompare("aze", "aze", ""));
    assertFalse(Utils.simpleCompare("aze", "aze55", ""));
    assertFalse(Utils.simpleCompare("aze33", "aze", "1"));
    assertTrue(Utils.simpleCompare("aze", "aze55", "53"));
    assertTrue(Utils.simpleCompare("aze759", "aze55", "53799"));
  }
}
