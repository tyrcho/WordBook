/* file name  : InvalidRuleException.java
 * authors    : MDA (Advantec)
 * created    : 23/10/2002 14:42:32
 * copyright  : 
 *
 * modifications:
 *
 */

package info.daviot.util.ruleparser;

/** 
 * Thrown when a rule is invalid.
 * 
 * @author MDA (Advantec)
 * @version CRDC2.0
 */
public class InvalidRuleException extends Exception {
    /** 
     * Constructs the exception.
     */
    public InvalidRuleException() {
        super();
    }

    /** 
     * Constructs the exception with a detailed message.
     * 
     * @param message the detailed message to describe the exception
     */
    public InvalidRuleException(String message) {
        super (message);
    }

}
