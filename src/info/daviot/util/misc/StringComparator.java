package info.daviot.util.misc;

import java.util.Comparator;

/**
 * Compare 2 chaines en fonction d'un parametre qui d�finit l'ordre
 * alphab�tique.
 */
public class StringComparator implements Comparator<String> {
	private String languageOrder;

	public StringComparator(String languageOrder) {
		this.languageOrder = languageOrder;
	}

	public int compare(String o1, String o2) {
		int m = Math.min(o1.length(), o2.length());
		for (int i = 0; i < m; i++) {
			char c1 = o1.charAt(i);
			char c2 = o2.charAt(i);
			int i1 = languageOrder.indexOf(c1);
			int i2 = languageOrder.indexOf(c2);
			if (i1 >= 0 && i2 >= 0) {//both in language
				if (i1 != i2) {
					return i1 - i2;
				}
			} else if (i1<0 && i2 <0) {//none in language
				if (c1!=c2) { return c1-c2;}
			} else {
				return i1>0 ? -1 : 1; //language comes first
			}			
		}
		return o1.length()-o2.length(); // short string first	
	}

}
