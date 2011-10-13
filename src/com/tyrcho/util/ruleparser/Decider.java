/* file name  : BusinessHoursManager.java
 * authors    : MDA (Advantec)
 * created    : 25/10/2002 17:17:44
 * copyright  : 
 *
 * modifications:
 *
 */

package com.tyrcho.util.ruleparser;

import java.util.* ;

/**
 * This class provides functionalities to handle Business Hours
 * linked to messages.
 * 
 * WARNING: Singleton pattern has been removed because we have more than one instance
 * across the project, each one having its own set of rules.
 * => It is now more a factory (Benoit Devos)
 * 
 * @author Michel Daviot (Adventec)
 * @version CRDC2.0
 */
public class Decider
{
	/**
	 * ALLOW rules are prioritary over DENY
	 */
	public static final String ALLOW_PRIORITY="allow";
	
	/**
	 * DENY rules are prioritary over ALLOW 
	 */
	public static final String DENY_PRIORITY="deny";
	
	/**
	 * No priority among rules : they are processed until a 
	 * matching one is found.
	 */
	public static final String NO_PRIORITY="no";

	private Vector rules;
	private String priority;
	private static Hashtable instances = new Hashtable() ;
	
	//hidden constructor
	public Decider(String rules, String priority) throws InvalidRuleException {
		this.rules=RulesParser.parseRules(rules);		
		this.priority=priority;
	}
	
	/**
	 * Tests if the message with given parameters is allowed
	 * to be processed.
	 * 
	 * @param parameters a Hashtable with parameter names as keys and values as values
	 * @return true if the message should be processed
	 * @throws NoMatchingRuleException if no according rule can answer the question
	 */
	public boolean isAllowed(Hashtable parameters) throws NoMatchingRuleException {
		Enumeration allRules=rules.elements();
		String tempResult=null;
		while (allRules.hasMoreElements()) {
			Rule rule=(Rule)allRules.nextElement();
			if (rule.isAllowed(parameters).equals(Rule.ALLOW)) {
				if (priority.equals(ALLOW_PRIORITY) || priority.equals(NO_PRIORITY)) {
					return true;
				} else {
					//DENY priority -> stores the result in case no deny is found
					tempResult="allow";
				}
			} else if (rule.isAllowed(parameters).equals(Rule.DENY)) {				
				if (priority.equals(DENY_PRIORITY) || priority.equals(NO_PRIORITY)) {
					return false;
				} else {
					//ALLOW priority -> stores the result in case no allow is found
					tempResult="deny";
				}
			} 
			//else IGNORE this rule : continue
		}
		if (tempResult==null) {
			// no matching rule is found
			if (priority.equals(DENY_PRIORITY)) {
				return false;
			} else if (priority.equals(ALLOW_PRIORITY)) {
				return true;
			} else {
				throw new NoMatchingRuleException ("No matching rule found in Decider.");		
			}
		} else {
			return tempResult.equals("allow");
		}
	}

	/**
	 * Return the result linked to the first rule evaluated to 'true'
	 * @param parameters a Hashtable with parameter names as keys and values as values
	 * @return the result linked to the rule
	 * @throws NoMatchingRuleException if no according rule can answer the question
	 */
	public String getFirstResult(Hashtable parameters) 
	throws NoMatchingRuleException
	{
		Enumeration allRules=rules.elements();
		while (allRules.hasMoreElements())
		{
			Rule rule = (Rule) allRules.nextElement();
			if (rule.isTrue(parameters)) return rule.getResult() ;
		}
		throw new NoMatchingRuleException ("No matching rule found in Decider.");		
	}

	/**
	 * This function will evaluates all the rules with the given parameters,
	 * and send results of the rules that match the parameters
	 * @param parameters a Hashtable with parameter names as keys and values as values
	 * @return the list of results
	 * @throws NoMatchingRuleException if no according rule can answer the question
	 */	
	public String [] getAllResults(Hashtable parameters) 
	throws NoMatchingRuleException
	{
		Enumeration allRules=rules.elements();
		Vector resultList = new Vector() ;
		
		while (allRules.hasMoreElements())
		{
			Rule rule = (Rule) allRules.nextElement();
			if (rule.isTrue(parameters)) resultList.add(rule.getResult()) ;
		}		
		
		if (resultList.size() == 0)
			throw new NoMatchingRuleException ("No matching rule found in Decider.");		
			
		String [] resultArray = new String[resultList.size()] ;
		return (String []) resultList.toArray(resultArray) ;
	}
	
	/**
	 * Gets the instance of the class (creates it if needed).
	 * 
	 * @param id the identifier for the instance
	 * @param rules a String containing all the rules
	 * @param priority defines if priority goes to allow or deny rules
	 * @return the unique instance of Decider
	 */
	public static Decider getInstance(String id, String rules, String priority)
	throws InvalidRuleException
	{
		Decider instance = (Decider) instances.get(id) ;
		if (instance==null)
		{
			instance=new Decider(rules, priority);
			instances.put(id, instance) ;
		}
		return instance;
	}

	/**
	 * Gets the instance of the class 
	 * (must have been instanciated first since it won't create it).
	 * 
	 * @param id the identifier for the instance
	 * @return the unique instance of Decider
	 * @throws NullPointerException if the Decider 
	 * has not been instanciated first
	 */
	public static Decider getInstance(String id)
	{
		Decider instance = (Decider) instances.get(id) ;
		if (instance==null)
		{
			throw new NullPointerException ("Decider has not been instanciated");
		}
		return instance;
	}
	
	public Vector getRules() 
	{
		return rules;
	}

}

