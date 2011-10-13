/* file name  : Test.java
 * authors    : MDA (Advantec)
 * created    : 23/10/2002 13:58:54
 * copyright  : 
 *
 * modifications:
 *
 */


package info.daviot.util.ruleparser;


import java.util.*;
import java.security.InvalidParameterException;


/** 
 * Represents a test on parameters.
 * 
 * @author MDA 
 */
class Test {
    /** 
     * Always true test.
     */
    public static final String TRUE="true";


    /** 
     * Always false test.
     */
    public static final String FALSE="false";


    /** 
     * Keyword to test the equality of a parameter and a constant.
     */
    public static final String IS="is";


    /** 
     * Keyword to test if a parameter starts with a word.
     */
    public static final String STARTSWITH="startsWith";


    /** 
     * Keyword to compare a parameter and a constant.
     */
    public static final String GT=">";


    /** 
     * Keyword to compare a parameter and a constant.
     */
    public static final String LT="<";


    /** 
     * Keyword to test the appartenance of a parameter in a set.
     */
    public static final String IN="in";


    /** 
     * Keyword to test if the parameter is between 2 constants.
     */
    public static final String BETWEEN="between";


	private String keyword;
	private Set set;
	private Value value1;
	private Value value2;


    /** 
     * Constructs a test with constant return value.
     * 
     * @param value the return value for this test (TRUE or FALSE)
     */
    public Test(String value) {
    	if (value.equalsIgnoreCase (TRUE) || value.equalsIgnoreCase (FALSE)) {
    		this.keyword=value;
       	} else {
       		throw new InvalidParameterException ("Invalid value for Test : "+value);
    	}    	
    }


    /** 
     * Constructs a test on a value and a set.
     * 
     * @param parameter the name of the parameter
     * @param keyword the keyword (IN, BETWEEN)
     * @param set the set or couple to use in the test
     */
    public Test(Value value, String keyword, Set set) {
    	if (keyword.equalsIgnoreCase (IN) || keyword.equalsIgnoreCase (BETWEEN)) {
			this.value1=value;
			this.set=set;
    		this.keyword=keyword;
       	} else {
       		throw new InvalidParameterException ("Invalid keyword for Test : "+keyword);
    	}
    }
            
    /** 
     * Constructs a test on 2 values.
     * 
     * @param value1 the first value
     * @param operator the operator ( IS, STARTSWITH, LT or GT )
     * @param value2 the other value
     */
    public Test(Value value1, String operator, Value value2) {
    	if (operator.equalsIgnoreCase (IS) 
    		|| operator.equalsIgnoreCase (STARTSWITH) 
    		|| operator.equalsIgnoreCase (LT) 
    		|| operator.equalsIgnoreCase (GT)) {
			this.value1=value1;
			this.value2=value2;
    		this.keyword=operator;
       	} else {
       		throw new InvalidParameterException ("Invalid operator for Test : "+operator);
    	}
    }
    
    /**
     * Tests this test for a given set of parameters.
     * 
     * @param parameters a Hashtable with the set of parameters
     * @return the result of the test
     * @throws NullPointerException if the internal state of the test is not correct
     */
    public boolean isTrue (Hashtable parameters) {
    	if (keyword.equalsIgnoreCase(TRUE) || keyword.equalsIgnoreCase(FALSE)) {
			//constant test
   			return keyword.equalsIgnoreCase(TRUE);
    	} else {
	    	if (keyword.equalsIgnoreCase(IN)) {
				return set.contains(value1.getValue(parameters));
 		   	} else if (keyword.equalsIgnoreCase(BETWEEN)) {
   				return set.hasBetween(value1.getValue(parameters));
    		} else if (keyword.equalsIgnoreCase(IS)) {
    			return value1.equals(value2, parameters);
	    	} else if (keyword.equalsIgnoreCase(STARTSWITH)) {
 		   		return value1.getValue(parameters).startsWith(value2.getValue(parameters));
  		  	} else if (keyword.equalsIgnoreCase(LT)) {
   				return value1.compareTo(value2, parameters)<0;
    		} else if (keyword.equalsIgnoreCase(GT)) {
    			return value1.compareTo(value2, parameters)>0;
	 	   	} else {
	  		  	throw new RuntimeException ("Invalid keyword in Test : "+keyword);    			
	    	}
    	}
    }

    public String toString() {
    	if (keyword.equalsIgnoreCase(TRUE) || keyword.equalsIgnoreCase(FALSE)) {
			//constant test
   			return keyword;
    	} else {
    		//test on parameter
	    	if (keyword.equalsIgnoreCase(IN)) {
				return value1+" in "+set;
 		   	} else if (keyword.equalsIgnoreCase(BETWEEN)) {
   				return value1+" between "+set;
    		} else if (keyword.equalsIgnoreCase(IS)) {
   				return value1+" is "+value2;
	    	} else if (keyword.equalsIgnoreCase(STARTSWITH)) {
   				return value1+" startswith "+value2;
  		  	} else if (keyword.equalsIgnoreCase(LT)) {
   				return value1+"<"+value2;
    		} else if (keyword.equalsIgnoreCase(GT)) {
   				return value1+">"+value2;
	 	   	} else {
	  		  	return "invalid test";
	    	}
    	}
    }
}
