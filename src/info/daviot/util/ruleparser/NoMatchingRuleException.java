package info.daviot.util.ruleparser;

/**
 * Thrown by BusinessHoursManager when no Rule allows to answer 
 * the question "should we process the message with this
 * set of parameter" ?
 */
public class NoMatchingRuleException extends Exception {

	/**
	 * Constructs an empty NoMatchingRuleException.
	 */
	public NoMatchingRuleException() {
		super();
	}

	/**
	 * Constructs a NoMatchingRuleException with detailed message.
	 * 
	 * @param message the detailed message
	 */
	public NoMatchingRuleException(String message) {
		super(message);
	}

}

