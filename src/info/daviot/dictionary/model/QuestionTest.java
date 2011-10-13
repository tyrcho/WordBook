package info.daviot.dictionary.model;

import info.daviot.dictionary.Utils;
import junit.framework.TestCase;


public class QuestionTest extends TestCase {
	public void testSimpleCompare() throws Exception {
		assertTrue(Utils.simpleCompare("a1z233e", "aze", "223 1"));
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
