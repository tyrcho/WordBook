/* file name  : RulesParser.java
 * authors    : MDA (Advantec)
 * created    : 23/10/2002 12:28:56
 * copyright  : 
 *
 * modifications:
 *
 */

package info.daviot.util.ruleparser;

import java.util.*;
import java.util.logging.Logger;

/** 
 * Allows to parse the set of rules in the configuration file to provide a set of rules to
 * the BusinessHoursManager.
 * <p>The grammar for these rules is :<br/>
 * <xmp>
 * RULE::=		CONDITION : ALLOW_TOKEN ;
 * 
 * ALLOW_TOKEN::= 	allow | deny
 * 
 * CONDITION::= 	not CONDITION
 * | CONDITION and CONDITION 
 * | CONDITION or CONDITION
 * | ( CONDITION )
 * | TEST
 * note: priority is given by default to the last conditions 
 * A or B and C is interpreted as A or (B and C)
 * 
 * TEST::=	   	VALUE in SET
 * | VALUE between COUPLE
 * | VALUE is VALUE
 * | VALUE &gt; VALUE
 * | VALUE &lt; VALUE
 * | VALUE startsWith VALUE
 * | true
 * | false
 * 
 * SET::=		( [CONSTANT [, CONSTANT [...]]] )
 * 
 * COUPLE::= 	( VALUE , VALUE )
 * 
 * VALUE ::=	CONSTANT | PARAMETER
 *
 * CONSTANT ::=	WORD | NUMBER
 * 
 * WORD::=		[a-zA-Z][a-zA-Z0-9]+ 
 * 
 * NUMBER::=	[1-9][0-9]*
 * 
 * PARAMETER::=	$weekDay 
 * | $yearDay
 * | $time
 * | $messageType
 * | $direction
 * | $olo
 * | $complexityClass
 * | $dn
 * </xmp> </p>
 * <p>
 * Example set for existing rules (with deny &gt; allow) :
 * $weekDay in (mon, wed, thu, fri) and $time between (800, 2020) and $direction is "in" : allow
 * $weekDay is tue and $time between (600, 2020) and $direction is "in" : allow
 * </p>
 * 
 * @author MDA (Advantec)
 * @version CRDC2.0
 */
public class RulesParser {
	private static String delimiters = "():><,;	 \r\n";
	private static String endRule = ";";
	private static String endCondition = ":";
	private static Logger logger=Logger.getLogger(RulesParser.class.getName());	
	private static String lineSeparator=System.getProperty("line.separator");
	private static String parameterDelimiter="$";
	
	private static boolean isParameter(String token)
	{
		return token.startsWith(parameterDelimiter);
	}    
	/** 
	 * Parses the given String to get the list of rules.
	 * 
	 * @param rules a String read from a config file with all the rules we want
	 * @return an array of Rules
	 * @throws InvalidRuleException if the grammar is not respected
	 */
	public static Vector parseRules(String rules) throws InvalidRuleException {
		debug("parsing rule strings"+lineSeparator+rules);		
		StringTokenizer stringTokenizer = new StringTokenizer(rules, delimiters, true);
		MyTokenizer tokenizer = (new RulesParser()).new MyTokenizer(stringTokenizer);
		Vector parsedRules = new Vector();

		while (tokenizer.hasMoreTokens()) {
			Rule rule = parseRule(tokenizer);
			parsedRules.add(rule);
		}
		return parsedRules;
	}

	/** 
	 * Parses the given String to get a rule.
	 * 
	 * @param ruleString a String with the rule we want
	 * @return a Rule
	 * @throws InvalidRuleException if the grammar is not respected
	 */
	public static Rule parseRule(String ruleString) throws InvalidRuleException {
		debug("parsing rule string"+lineSeparator+ruleString);
		StringTokenizer stringTokenizer = new StringTokenizer(ruleString, delimiters, true);
		MyTokenizer tokenizer = (new RulesParser()).new MyTokenizer(stringTokenizer);
		Rule rule = parseRule(tokenizer);
		if (tokenizer.hasMoreTokens()) {
			String token=tokenizer.getNextToken();
			throw new InvalidRuleException(
				"No more tokens expected after parsing rule #"
				+ruleString
				+"# : token #"
				+token
				+"# unexpected.");
		} else {
			return rule;
		}
	}


	private static Rule parseRule(MyTokenizer tokenizer)
	throws InvalidRuleException {
		try
		{
			Condition condition = parseCondition(tokenizer);
			tokenizer.dropToken(endCondition);
			String allowed = tokenizer.getNextToken();
			tokenizer.dropToken(endRule);
			Rule rule=new Rule(condition, allowed);
			logger.info("rule parsed "+lineSeparator+rule);
			return rule;
		}
		catch (InvalidRuleException e)
		{
			throw new InvalidRuleException(e.getMessage()+" after "+lineSeparator+tokenizer.getReadTokens());
		}
	}

	//note: priority is granted to the last nested tests
	//A and B or C is considered as (A and (B or C))
	private static Condition parseCondition(MyTokenizer tokenizer)
		throws InvalidRuleException {
		String token = tokenizer.getNextToken();
		Condition condition = null;
		if (token.equalsIgnoreCase(Condition.OPEN_PRIORITY)) {
			//special : condition priority
			condition = parseCondition(tokenizer);
			tokenizer.dropToken(Condition.CLOSE_PRIORITY);
		} else if (token.equalsIgnoreCase(Condition.NOT)) {
			//special : NOT condition
			Condition nestedCondition = parseCondition(tokenizer);
			condition = new Condition(nestedCondition, Condition.NOT);
		} else {
			//default : test condition
			tokenizer.rollBackToken();
			condition = new Condition(parseTest(tokenizer));
		}

		//treats AND / OR
		String keyword = tokenizer.getNextToken();
		if (keyword.equalsIgnoreCase(Condition.AND) || keyword.equalsIgnoreCase(Condition.OR)) {
			Condition nestedCondition = parseCondition(tokenizer);
			return new Condition(condition, nestedCondition, keyword);
		} else {
			tokenizer.rollBackToken();
			return condition;
		}

	}

	/*
	   private static Vector processRepeatedConditions(String logical, MyTokenizer tokenizer) {
	   Vector conditions=new Vector();
	   for (String keyWord=logical; keyWord.equals(logical); keyWord=tokenizer.getNextToken()) {
	   }
	   return conditions.size==0 ? null : conditions;
	   }
	   */

	private static Test parseTest(MyTokenizer tokenizer)
		throws InvalidRuleException {
		String token = tokenizer.getNextToken();
		if (token.equalsIgnoreCase(Test.TRUE) || token.equalsIgnoreCase(Test.FALSE)) {
			//constant test
			return new Test(token);
		} else {
			Value value = parseValue(token);
			//test on values
			String keyword = tokenizer.getNextToken();
 			if (keyword.equalsIgnoreCase(Test.IN) || keyword.equalsIgnoreCase(Test.BETWEEN)) {
 				//test against a set of values
				Set set = parseSet(tokenizer);
				return new Test(value, keyword, set);
			} else if (keyword.equalsIgnoreCase(Test.IS)
					|| keyword.equalsIgnoreCase(Test.STARTSWITH)
					|| keyword.equalsIgnoreCase(Test.GT)
					|| keyword.equalsIgnoreCase(Test.LT)) {
				//test against another value
				String token2 = tokenizer.getNextToken();
				Value value2=parseValue(token2);
				return new Test(value, keyword, value2);
			} else {
				throw new InvalidRuleException("Unexpected keyword : #" + keyword +"#");
			}	
		}
	}
	
	private static Value parseValue(String token)
		throws InvalidRuleException 
	{
		if (isParameter(token))
		{
			return new Value(token.substring(parameterDelimiter.length()), false);
		}
		else
		{
			return new Value(token, true);
		}
	}
	private static Set parseSet(MyTokenizer tokenizer)
		throws InvalidRuleException {
		Vector constants = new Vector();
		tokenizer.dropToken(Set.OPEN_SET);
		String token = tokenizer.getNextToken();
		while (!token.equalsIgnoreCase(Set.CLOSE_SET)) {
			while (token.equalsIgnoreCase(Set.SET_SEPARATOR)) {
				token = tokenizer.getNextToken();
			}
			constants.add(token);
			token = tokenizer.getNextToken();
		}
		return new Set(constants);
	}

	private static void debug(String message)
	{
	    logger.info(message);
	}
	public class MyTokenizer {
		private Vector tokens;
		private int currentPosition;
		private int maxPosition;
		private String initialData;

		public MyTokenizer(StringTokenizer tokenizer) {
			tokens = new Vector();
			currentPosition = 0;
			StringBuffer buffer = new StringBuffer();
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (!token.trim().equals("")) {
					tokens.add(token);
				}
				buffer.append(token);
			}
			initialData = buffer.toString();
			maxPosition = tokens.size() - 1;
		}

		public void dropToken(String token) throws InvalidRuleException {
			String previousToken;
			if (currentPosition > 0 && maxPosition > 0) {
				previousToken = (String) tokens.get(currentPosition - 1);
			} else {
				previousToken = "!no previous token!";
			}

			if (hasMoreTokens()) {
				String nextToken = (String) tokens.get(currentPosition);
				currentPosition++;
				if (!token.equals(nextToken)) {
					throw new InvalidRuleException(
						"Invalid token : #"
							+ nextToken
							+ "# : token #"
							+ token
							+ "# was expected after token #"
							+ previousToken
							+ "# for rule(s) #"
							+ initialData
							+ "#");
				}
			} else {
					throw new InvalidRuleException(
						"Missing token : #"
							+ token
							+ "# was expected after token #"
							+ previousToken
							+ "# for rule(s) #"
							+ initialData
							+ "#");				
			}
		}

		public String getNextToken() throws InvalidRuleException {
			if (hasMoreTokens()) {
				String token = (String) tokens.get(currentPosition);
				currentPosition++;
//				debug("position : "+currentPosition+", token : "+token);
				return token;
			} else {
				throw new InvalidRuleException(
					"End of string invalid in " + initialData + ": more token(s) expected.");
			}
		}

		public boolean hasMoreTokens() {
			return currentPosition <= maxPosition;
		}

		public void rollBackToken() {
			if (currentPosition > 0) {
				currentPosition--;
			}
		}
		
		public String getReadTokens()
		{
			StringBuffer buffer=new StringBuffer();
			for (int i=0; i<currentPosition; i++)
			{
				buffer.append(tokens.get(i));
				buffer.append(" ");
			}
			return buffer.toString();
		}
	}
}
