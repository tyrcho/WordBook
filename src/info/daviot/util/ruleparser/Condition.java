/* file name  : Condition.java
 * authors    : MDA (Advantec)
 * created    : 23/10/2002 12:53:27
 * copyright  : 
 *
 * modifications:
 *
 */

package info.daviot.util.ruleparser;

import java.util.*;
import java.security.InvalidParameterException;

/** 
 * Represents a condition to test in a rule.
 * 
 * @author MDA (Advantec)
 * @version CRDC2.0
 */
class Condition {	
    /** 
     * Keyword for linking conditions with logical and.
     */
    public static final String AND="and";
    
    /** 
     * Keyword for linking conditions with logical or.
     */
    public static final String OR="or";
    
    /** 
     * Keyword for linking conditions with logical not.
     */
    public static final String NOT="not";

    /** 
     * Opens the priority group.
     */
    public static final String OPEN_PRIORITY="(";

    /** 
     * Closes the priority group.
     */
    public static final String CLOSE_PRIORITY=")";

	private static final int BINARY_TYPE=1; //AND or OR
	private static final int UNARY_TYPE=2; //NOT
	private static final int TEST_TYPE=3; //Test

	private Condition condition1;
	private Condition condition2;
	private String keyword;
	private Test test;
	private int conditionType;

    /** 
     * Constructs a Condition linking 2 conditions (AND or OR).
     * 
     * @param condition1 
     * @param condition2 
     * @param keyword AND or OR is expected
     * @throws InvalidParameterException if another keyword is used
     */
    public Condition(Condition condition1, Condition condition2, String keyword) {
    	if (keyword.equalsIgnoreCase (AND) || keyword.equalsIgnoreCase(OR)) {
    		this.condition1=condition1;
    		this.condition2=condition2;
    		this.keyword=keyword;
    		conditionType=BINARY_TYPE;
       	} else {
       		throw new InvalidParameterException ("Invalid keyword for Condition : "+keyword);
    	}
    }

    /** 
     * Constructs a Condition linking one condition with a keyword (NOT).
     * 
     * @param condition 
     * @param keyword NOT is expected
     * @throws InvalidParameterException if another keyword is used
     */
    public Condition(Condition condition, String keyword) {
    	if (keyword.equalsIgnoreCase (NOT)) {
    		this.condition1=condition;
    		this.keyword=keyword;
    		conditionType=UNARY_TYPE;
       	} else {
       		throw new InvalidParameterException ("Invalid keyword for Condition : "+keyword);
    	}
    }

    /** 
     * Constructs a condition with a test.
     * 
     * @param test the test to use in the condition
     */
    public Condition(Test test) {
    	this.test=test;
    	conditionType=TEST_TYPE;
    }
    
    public boolean isTrue(Hashtable parameters) {
    	switch (conditionType) {
    		case UNARY_TYPE : 
    			if (keyword.equalsIgnoreCase(NOT)) {
    				return !condition1.isTrue(parameters);
    			} else {
    				throw new RuntimeException ("Invalid state in Condition : Unary without NOT");
    			}
    		case BINARY_TYPE : 
    			if (keyword.equalsIgnoreCase(OR)) {
    				return condition1.isTrue(parameters) || condition2.isTrue(parameters) ;
    			} else if (keyword.equalsIgnoreCase(AND)) {
    				return condition1.isTrue(parameters) && condition2.isTrue(parameters) ;
    			} else {
    				throw new RuntimeException ("Invalid state in Condition : Binary without OR or AND");
    			}
    		case TEST_TYPE : 
    			return test.isTrue(parameters);
    		default :
	    		throw new RuntimeException ("Invalid state in Condition : "+conditionType);    			
    	}
    }
    
    public String toString() {
    	switch (conditionType) {
    		case UNARY_TYPE : 
    			return "("+keyword+" "+condition1+")";
    		case BINARY_TYPE : 
    		    return "("+condition1+" "+keyword+" "+condition2+")";
    		case TEST_TYPE :     		    
    			return "("+test.toString()+")";
    		default :
	    		return "invalid condition";			
    	}
    }

}
