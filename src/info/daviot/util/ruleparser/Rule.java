/* file name  : Rule.java
 * authors    : MDA (Advantec)
 * created    : 23/10/2002 12:39:14
 * copyright  : 
 *
 * modifications:
 *
 */

package info.daviot.util.ruleparser;

import java.security.InvalidParameterException;
import java.util.*;

/** 
 * Represents a single business hours rule.
 * A rule determines whether a message should be processed or not at a given time.
 * 
 * @author MDA (Advantec)
 * @version CRDC2.0
 */
public class Rule {
    /** 
     * Constant for allowed message.
     */
    public static final String ALLOW="allow";
    /** 
     * Constant for denied message.
     */
    public static final String DENY="deny";
    /** 
     * Constant for ignored rule (no match with parameters).
     */
    public static final String IGNORE="ignore";

	private Condition condition;
	private String result;

    /** 
     * Processes the current rule for a set of parameters.
     * 
     * @param parameters a Hashtable with the message parameters
     * @return ALLOW, DENY or IGNORE
     */
    public String isAllowed (Hashtable parameters)
    {
		return condition.isTrue(parameters) ? result : IGNORE ;
    }

	/**
	 * Evaluate the condition linked to the rule
	 * @return true if the Condition is true
	 * @return false if the Condition is false
	 */
	public boolean isTrue(Hashtable parameters)
	{
		return condition.isTrue(parameters) ;
	}
	
	public String getResult()
	{
		return result ;
	}
	
    /** 
     * Constructs a rule for the given condition and allowed value.
     * 
     * @param condition the Condition object to use
     * @param allow ALLOW or DENY
     * @throws InvalidParameterException if another keyword is used
     */
    Rule(Condition condition, String result)
    {
	    	this.condition=condition;    	
 		   	this.result=result;
    }
    
    public String toString()
    {
    	return condition+" : "+result;
    }
}
