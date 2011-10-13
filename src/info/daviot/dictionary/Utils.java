package info.daviot.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;

public class Utils {
	public static String read(String fileName) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(fileName),
				DictionaryConstants.CHARSET);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String read = bufferedReader.readLine();
		StringBuilder builder = new StringBuilder();
		while (read != null) {
			builder.append(read);
			read = bufferedReader.readLine();
		}
		return builder.toString();
	}

	// comparaison sans les caract�res ignor�s
	public static boolean simpleCompare(String s1, String s2,
			String ignoredChars) {
		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();
		Collection<Character> ignored = new HashSet<Character>();
		if (ignoredChars != null) {
			for (Character character : ignoredChars.toCharArray()) {
				ignored.add(character);
			}
		}
		int i = 0;
		for (char c : c1) {
			if (!ignored.contains(c)) {
				try {
					while (ignored.contains(c2[i])) {
						i++;
					}
				} catch (IndexOutOfBoundsException e) {
					return false;
				}

				if (c != c2[i++]) {
					return false;
				}
			}
		}
		while (i < c2.length && ignored.contains(c2[i])) {
			i++;
		}
		return i == c2.length;
	}
	
	
}
