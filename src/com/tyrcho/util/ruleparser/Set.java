/* file name  : Set.java
 * authors    : MDA (Advantec)
 * created    : 23/10/2002 14:21:05
 * copyright  : 
 *
 * modifications:
 *
 */

package com.tyrcho.util.ruleparser;

import java.util.*;

/** 
 * Represents a set of constants to use in tests.
 * A couple is a set with 2 elements.
 * 
 * @author MDA (Advantec)
 * @version CRDC2.0
 */
class Set {
    /** 
     * Opens a set.
     */
    public static final String OPEN_SET="(";
    
    /** 
     * Closes the set.
     */
    public static final String CLOSE_SET=")";
    
    /** 
     * Separates items in the set.
     */
    public static final String SET_SEPARATOR=",";
    
    private Vector constants;
    
    /** 
     * Constructs a Set of constants.
     * 
     * @param constants a Vector of String
     * @throws NullPointerException if a constant is empty
     */
    public Set(Vector constants) {
    	this.constants=new Vector();
    	Enumeration elements=constants.elements();
    	while(elements.hasMoreElements()) {
			//only adds non empty elements
		   	String element=(String)elements.nextElement();
 		   	if (element==null || element.equals("")) {
 		   		throw new NullPointerException ("Empty constant in set");
 		   	} else {
 		   		this.constants.add(element);
 		   	}
    	}
    }
    
    /** 
     * Tests if this set contains a given element.
     * 
     * @param element a String with the element to test
     * @return true if the element is in the set
     */
    public boolean contains(String element) {
    	return constants.contains(element);
    }
    
    /** 
     * Tests if the element is between the 2 parameters of this couple.
     * 
     * @param element a String with the element to test
     * @return true if the element is between
     * @throws RuntimeException if the set doesn't have 2 elements
     */
    public boolean hasBetween(String element) {
    	if (constants.size() != 2) {
    		throw new RuntimeException ("This set doesn't have 2 elements");
    	}
    	
    	boolean afterFirst=element.compareTo((String)constants.get(0))>0;
    	boolean beforeLast=element.compareTo((String)constants.get(1))<0;
    	
    	return afterFirst && beforeLast;
    }
    
    public String toString()
    {
    	StringBuffer buffer=new StringBuffer();
    	buffer.append("(");
    	Iterator i=constants.iterator();
    	boolean isFirst=true;
    	while (i.hasNext())
    	{
    		if (!isFirst)
    		{
	    		buffer.append(",");
    		}
    		isFirst=false;
    		buffer.append(i.next());
    	}
        buffer.append(")");
        return buffer.toString();
    }
   
}
